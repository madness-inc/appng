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

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.appng.api.ValidationMessages;
import org.appng.api.model.Permission;

/**
 * Default {@link Permission}-implementation
 * 
 * @author Matthias Müller
 */
@Entity
@Table(name = "permission")
@EntityListeners(PlatformEventListener.class)
public class PermissionImpl implements Permission, Auditable<Integer> {

	private Integer id;
	private String name;
	private String description;
	private Date version;
	private ApplicationImpl application;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull(message = ValidationMessages.VALIDATION_NOT_NULL)
	@Pattern(regexp = ValidationPatterns.PERMISSON_PATTERN, message = ValidationPatterns.PERMISSON_MSSG)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Size(max = ValidationPatterns.LENGTH_8192, message = ValidationMessages.VALIDATION_STRING_MAX)
	@Column(length = ValidationPatterns.LENGTH_8192)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Version
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@ManyToOne(targetEntity = ApplicationImpl.class)
	public ApplicationImpl getApplication() {
		return application;
	}

	public void setApplication(ApplicationImpl application) {
		this.application = application;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this);
	}

	@Override
	public boolean equals(Object o) {
		return ObjectUtils.equals(this, o);
	}

	@Override
	public String toString() {
		return "Permission#" + getId() + "_" + getName();
	}
}
