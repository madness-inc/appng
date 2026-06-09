/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.appng.forms.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.appng.forms.FormUpload;
import org.appng.forms.Request;
import org.appng.forms.XSSUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * The default {@link Request}-implementation.
 * 
 * @author Matthias Müller
 */
@Slf4j
public class RequestBean implements Request {

	private static final String POST = "POST";
	private static final String GET = "GET";
	private static final String UTF_8 = "UTF-8";
	private boolean isMultiPart;
	private String encoding;
	private File tempDir;
	private long maxSize;
	private String method;
	private static final long MAX_SIZE = 10L * 1024 * 1024;
	private Map<String, List<String>> uploadFileTypes;
	private boolean sizeStrict;
	private boolean isValid;
	private XSSUtil xssUtil;
	private HttpServletRequest httpServletRequest;
	protected Map<String, List<FormUpload>> formUploads;
	protected Map<String, List<String>> parameters;
	protected String host;

	public RequestBean() {
		this(MAX_SIZE);
	}

	public RequestBean(long maxSize) {
		this(maxSize, null);
	}

	public RequestBean(long maxSize, File tempDir) {
		this.encoding = UTF_8;
		this.tempDir = tempDir;
		this.maxSize = maxSize;
		this.parameters = new HashMap<>();
		this.uploadFileTypes = new HashMap<>();
		this.formUploads = new HashMap<>();
	}

	public List<FormUpload> getFormUploads(String name) {
		if (formUploads.containsKey(name)) {
			return Collections.unmodifiableList(formUploads.get(name));
		} else {
			return Collections.unmodifiableList(new ArrayList<>());
		}
	}

	public void process(HttpServletRequest servletRequest) {
		this.httpServletRequest = servletRequest;
		setHost(servletRequest.getServerName());
		if (null == tempDir || !tempDir.exists()) {
			tempDir = new File(System.getProperty("java.io.tmpdir"));
		}
		LOGGER.debug("tempdir is {}", tempDir.getAbsolutePath());

		try {
			LOGGER.debug("content type: {}", httpServletRequest.getContentType());
			LOGGER.debug("requestURI: {}", httpServletRequest.getRequestURI());
			LOGGER.debug("contextPath: {}", httpServletRequest.getContextPath());
			LOGGER.debug("servletPath: {}", httpServletRequest.getServletPath());
			LOGGER.debug("pathInfo: {}", httpServletRequest.getPathInfo());

			this.method = httpServletRequest.getMethod().toUpperCase();
			LOGGER.debug("request method: {}", method);

			String contentType = httpServletRequest.getContentType();
			isMultiPart = contentType != null && contentType.toLowerCase().contains("multipart/");
			boolean stripXss = stripXss();
			if (isMultiPart) {
				if (null != httpServletRequest.getAttribute(REQUEST_PARSED)) {
					LOGGER.info("the multipart-request {} has been parsed before, parsing is skipped",
							httpServletRequest);
					return;
				}
				// POST, multipart/form-data
				Collection<Part> parts = httpServletRequest.getParts();
				Charset charset = Charset.forName(encoding);
				for (Part part : parts) {
					String name = part.getName();
					String submittedFileName = part.getSubmittedFileName();
					boolean isFormField = submittedFileName == null || submittedFileName.isEmpty();

					if (isFormField) {
						String value = new String(part.getInputStream().readAllBytes(), charset);
						if (stripXss) {
							value = xssUtil.stripXss(value);
						}
						List<String> list = parameters.get(name);
						if (list == null) {
							list = new ArrayList<>();
							parameters.put(name, list);
						} else {
							LOGGER.trace("{} parameter: {} is multi-valued", method, name);
						}
						list.add(value);
						LOGGER.trace("{} parameter: {} = {}", method, name, value);
					} else {
						if (!formUploads.containsKey(name)) {
							formUploads.put(name, new ArrayList<>());
						}
						if (part.getSize() > 0) {
							if (sizeStrict && part.getSize() > maxSize) {
								throw new IllegalStateException(
										"Upload for '" + name + "' exceeds max size: " + part.getSize() + " > " + maxSize);
							}
							String extension = FilenameUtils.getExtension(submittedFileName);
							int i = 0;
							String sessionId = httpServletRequest.getSession().getId();
							File outFile = getOutFile(sessionId, extension, i);
							while (outFile.exists()) {
								i++;
								outFile = getOutFile(sessionId, extension, i);
							}
							try (InputStream in = part.getInputStream();
									OutputStream out = new FileOutputStream(outFile)) {
								in.transferTo(out);
							}
							List<String> acceptedTypes = getAcceptedTypes(name);
							FormUpload formUpload = new FormUploadBean(outFile, submittedFileName,
									part.getContentType(), acceptedTypes, maxSize);
							formUploads.get(name).add(formUpload);
							LOGGER.trace("{} upload parameter: {}", method, formUpload);
						} else {
							LOGGER.debug("nothing uploaded for field {}", name);
						}
					}
				}
				servletRequest.setAttribute(REQUEST_PARSED, this);
			} else {
				// POST, application/x-www-form-urlencoded
				// or GET
				Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					String name = parameterNames.nextElement();
					String[] parameterValues = httpServletRequest.getParameterValues(name);
					if (stripXss) {
						parameterValues = xssUtil.stripXss(parameterValues);
					}
					List<String> values = new ArrayList<>(Arrays.asList(parameterValues));
					if (values.size() > 1) {
						LOGGER.trace("{} parameter: {} is multi-valued", method, name);
					}
					parameters.put(name, values);
					LOGGER.trace("{} parameter: {} = {}", method, name, values);
				}
			}
			if (xssEnabled()) {
				xssUtil.setProcessed(servletRequest, stripXss);
			}
			this.isValid = true;
		} catch (Exception e) {
			this.isValid = false;
			LOGGER.error("Error while processing form data: ", e);
		}

	}

	private boolean stripXss() {
		return xssEnabled() && xssUtil.doProcess(httpServletRequest);
	}

	private boolean xssEnabled() {
		return null != xssUtil;
	}

	private File getOutFile(String sessionId, String extension, int count) {
		return new File(tempDir, sessionId + "_" + count + "." + extension);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isMultiPart() {
		return isMultiPart;
	}

	public boolean isPost() {
		return POST.equalsIgnoreCase(method);
	}

	public boolean isGet() {
		return GET.equalsIgnoreCase(method);
	}

	public void setTempDir(File tempDir) {
		this.tempDir = tempDir;
	}

	public void setMaxSize(long maxSize) {
		setMaxSize(maxSize, false);
	}

	public void setMaxSize(long maxSize, boolean isStrict) {
		this.maxSize = maxSize;
		this.sizeStrict = isStrict;
	}

	public void setAcceptedTypes(String uploadName, String... types) {
		if (!uploadFileTypes.containsKey(uploadName)) {
			uploadFileTypes.put(uploadName, new ArrayList<>());
		}
		uploadFileTypes.get(uploadName).clear();
		if (null != types) {
			for (String extension : types) {
				uploadFileTypes.get(uploadName).add(extension.toLowerCase());
			}
		}
	}

	public List<String> getAcceptedTypes(String uploadName) {
		return uploadFileTypes.get(uploadName);
	}

	public boolean isValid() {
		return isValid;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void addParameter(String key, String value) {
		addParameters(key, Arrays.asList(value));
	}

	public void addParameters(String key, List<String> values) {
		if (!parameters.containsKey(key)) {
			parameters.put(key, Collections.unmodifiableList(values));
			LOGGER.debug("adding {} new value(s) for parameter '{}'", values.size(), key);
		} else {
			LOGGER.warn("parameter '{}' is alread present, tried to add {} new value(s)", key, values.size());
		}
	}

	public void addParameters(Map<String, String> singleParameters) {
		for (String key : singleParameters.keySet()) {
			addParameter(key, singleParameters.get(key));
		}
	}

	public Map<String, List<FormUpload>> getFormUploads() {
		return Collections.unmodifiableMap(formUploads);
	}

	public String getHost() {
		return host;
	}

	public String getParameter(String name) {
		return getSingleParameter(name);
	}

	public List<String> getParameterList(String name) {
		List<String> list = parameters.get(name);
		if (null == list) {
			list = new ArrayList<>();
		}
		return Collections.unmodifiableList(list);
	}

	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	public Map<String, String> getParameters() {
		Set<String> keySet = parameters.keySet();
		Map<String, String> map = new HashMap<>();
		for (String key : keySet) {
			String value = getSingleParameter(key);
			if (null != value) {
				map.put(key, value);
			}
		}
		return Collections.unmodifiableMap(map);
	}

	public Map<String, List<String>> getParametersList() {
		return Collections.unmodifiableMap(parameters);
	}

	String getSingleParameter(String name) {
		String value = null;
		List<String> list = parameters.get(name);
		if (list != null) {
			int size = list.size();
			if (size > 0) {
				value = list.get(0);
			}
			if (size > 1) {
				LOGGER.trace("parameter '{}' is multi-valued, discarding value(s) {}", name, list.subList(1, size));
			}
		}
		return value;
	}

	public boolean hasParameter(String name) {
		return getParameterNames().contains(name);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public XSSUtil getXssUtil() {
		return xssUtil;
	}

	public void setXssUtil(XSSUtil xssUtil) {
		this.xssUtil = xssUtil;
	}

}
