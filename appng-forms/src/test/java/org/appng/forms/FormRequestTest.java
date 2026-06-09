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
package org.appng.forms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.appng.forms.impl.RequestBean;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FormRequestTest {

	private static final String SESSION_ID = "5F2ACDC183D81BEFC73E7B7482ECEAA4";

	@Test
	public void testUploadFileByContentType() throws Exception {
		testMultipart("image/jpeg");
	}

	@Test
	public void testUploadFileByExtension() throws Exception {
		testMultipart("GIF", "JPG");
	}

	private void testMultipart(String... fileTypes) throws Exception {
		String bar = "bar";
		String foo = "foo";
		String file = "file";
		String filename = "anonymous.jpg";
		String filenameComplete = "c:\\foo\bar\\" + filename;
		String contentType = "image/jpeg";

		Request formRequest = new RequestBean();
		HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(httpRequest.getCharacterEncoding()).thenReturn("UTF-8");
		Mockito.when(httpRequest.getHeader("Content-Encoding")).thenReturn("UTF-8");
		Mockito.when(httpRequest.getContentType()).thenReturn("multipart/form-data; boundary=foobar");
		Mockito.when(httpRequest.getMethod()).thenReturn("POST");

		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(session.getId()).thenReturn(SESSION_ID);
		Mockito.when(httpRequest.getSession()).thenReturn(session);

		ClassLoader classLoader = FormRequestTest.class.getClassLoader();
		File originalFile = new File(classLoader.getResource("deathstar.jpg").getFile());
		byte[] fileBytes = java.nio.file.Files.readAllBytes(originalFile.toPath());

		Part fooPart = Mockito.mock(Part.class);
		Mockito.when(fooPart.getName()).thenReturn(foo);
		Mockito.when(fooPart.getSubmittedFileName()).thenReturn(null);
		Mockito.when(fooPart.getInputStream()).thenReturn(new ByteArrayInputStream(bar.getBytes("UTF-8")));

		Part filePart = Mockito.mock(Part.class);
		Mockito.when(filePart.getName()).thenReturn(file);
		Mockito.when(filePart.getSubmittedFileName()).thenReturn(filenameComplete);
		Mockito.when(filePart.getContentType()).thenReturn(contentType);
		Mockito.when(filePart.getSize()).thenReturn((long) fileBytes.length);
		Mockito.when(filePart.getInputStream()).thenReturn(new ByteArrayInputStream(fileBytes));

		Mockito.when(httpRequest.getParts()).thenReturn(Arrays.asList(fooPart, filePart));

		formRequest.setMaxSize(10551);

		formRequest.setAcceptedTypes(file, fileTypes);
		formRequest.process(httpRequest);

		Assert.assertEquals(true, formRequest.isMultiPart());

		Assert.assertEquals(bar, formRequest.getParametersList().get(foo).get(0));
		Assert.assertEquals(bar, formRequest.getParameter(foo));

		Map<String, List<FormUpload>> formUploads = formRequest.getFormUploads();
		Assert.assertEquals(1, formUploads.size());
		FormUpload formUpload = formRequest.getFormUploads(file).get(0);
		Assert.assertEquals(formUpload, formUploads.get(file).get(0));
		Assert.assertEquals(filename, formUpload.getOriginalFilename());
		Assert.assertEquals(true, formUpload.isValid());
		Assert.assertEquals(true, formUpload.isValidSize());

		Assert.assertEquals(true, FileUtils.contentEquals(originalFile, formUpload.getFile()));
		Assert.assertEquals(contentType, formUpload.getContentType());
	}

	@Test
	public void testGet() throws Exception {
		String foo = "foo";
		String bar = "bar";
		String answer = "42";

		final Map<String, List<String>> requestParameters = new HashMap<>();
		List<String> values = new ArrayList<>();
		values.add(bar);
		values.add(answer);
		requestParameters.put(foo, values);

		Request formRequest = new RequestBean();
		HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(httpRequest.getCharacterEncoding()).thenReturn("UTF-8");
		Mockito.when(httpRequest.getHeader("Content-Encoding")).thenReturn("UTF-8");
		Mockito.when(httpRequest.getMethod()).thenReturn("GET");

		Mockito.when(httpRequest.getParameterValues(Mockito.anyString())).thenAnswer(new Answer<String[]>() {
			public String[] answer(InvocationOnMock invocation) throws Throwable {
				String name = (String) invocation.getArguments()[0];
				List<String> list = requestParameters.get(name);
				return list.toArray(new String[list.size()]);
			}
		});
		final Iterator<String> it = requestParameters.keySet().iterator();
		Mockito.when(httpRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
			public String nextElement() {
				return it.next();
			}

			public boolean hasMoreElements() {
				return it.hasNext();
			}
		});

		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(session.getId()).thenReturn(SESSION_ID);
		Mockito.when(httpRequest.getSession()).thenReturn(session);

		formRequest.process(httpRequest);

		Assert.assertEquals(false, formRequest.isMultiPart());
		Assert.assertEquals(0, formRequest.getFormUploads().size());
		Assert.assertEquals(bar, formRequest.getParameter(foo));
		Assert.assertEquals(values, formRequest.getParameterList(foo));
	}
}
