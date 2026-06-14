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
package org.appng.core.domain;

import java.io.File;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import org.apache.commons.codec.digest.DigestUtils;
import org.appng.api.model.Resource;
import org.appng.api.model.ResourceType;

/**
 * Representats a {@link Resource} of an appNG template.
 * 
 * @author Matthias Müller
 */
@Entity
@Table(name = "template_resource")
public class TemplateResource implements Resource {

	private Integer id;
	private String name;
	private String description;
	private LocalDateTime fileVersion;
	private LocalDateTime version;
	private Template template;
	private ResourceType type = ResourceType.RESOURCE;
	private byte[] bytes;
	private File cachedFile;
	private String checkSum;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(LocalDateTime fileVersion) {
		this.fileVersion = fileVersion;
	}

	@Version
	public LocalDateTime getVersion() {
		return version;
	}

	public void setVersion(LocalDateTime version) {
		this.version = version;
	}

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK__TEMPLATE_RESOURCE__TEMPLATE_ID"))
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Transient
	public ResourceType getResourceType() {
		return type;
	}

	public void setResourceType(ResourceType type) {
		this.type = type;
	}

	@Lob
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Transient
	public File getCachedFile() {
		return cachedFile;
	}

	public void setCachedFile(File cachedFile) {
		this.cachedFile = cachedFile;
	}

	@Column(name = "checksum")
	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	@Transient
	public int getSize() {
		return null == bytes ? 0 : bytes.length;
	}

	public String calculateChecksum() {
		if (getSize() > 0) {
			setCheckSum(DigestUtils.sha256Hex(getBytes()));
		}
		return getCheckSum();
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this);
	}

	@Override
	public boolean equals(Object o) {
		return ObjectUtils.equals(this, o);
	}
}
