package org.saphka.entity.extension.model;

import org.saphka.entity.extension.configuration.DynamicExtensionSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = DynamicExtensionSettings.FIELD_TABLE_NAME)
public class Field {

	@Id
	@Column(name = "GUID")
	private UUID id;

	@Column(name = "EXTENSION_ID")
	private String extensionId;

	@Column(name = "FIELD_NAME")
	private String fieldName;

	@Column(name = "FIELD_TYPE")
	private String fieldType;

	@Column(name = "FIELD_LENGTH")
	private Long fieldLength;

	@Column(name = "FIELD_FRACTION")
	private Long fieldFraction;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Long getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Long fieldLength) {
		this.fieldLength = fieldLength;
	}

	public Long getFieldFraction() {
		return fieldFraction;
	}

	public void setFieldFraction(Long fieldFraction) {
		this.fieldFraction = fieldFraction;
	}
}
