package org.saphka.entity.extension.model;

import org.saphka.entity.extension.configuration.DynamicExtensionSettings;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = DynamicExtensionSettings.FIELD_TABLE_NAME)
public class Field {

	@Id
	@Column(name = "GUID")
	@NotNull
	private UUID id;

	@Column(name = "EXTENSION_ID")
	@Size(min = 1, max = 200)
	@NotNull
	private String extensionId;

	@Column(name = "FIELD_NAME")
	@Size(min = 1, max = 50)
	@NotNull
	private String fieldName;

	@Column(name = "FIELD_TYPE")
	@Enumerated(EnumType.STRING)
	@Size(min = 1, max = 50)
	@NotNull
	private FieldType fieldType;

	@Column(name = "FIELD_LENGTH")
	@NotNull
	private Long fieldLength;

	@Column(name = "FIELD_FRACTION")
	@NotNull
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

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
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
