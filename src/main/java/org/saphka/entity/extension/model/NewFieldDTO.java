package org.saphka.entity.extension.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * @author Alex Loginov
 */
public class NewFieldDTO {

	@NotNull
	@Size(min = 1, max = 200)
	private final String extensionId;
	@Size(min = 1, max = 50)
	@NotNull
	private final String fieldName;
	@Size(min = 1, max = 50)
	@NotNull
	private final FieldType fieldType;
	@NotNull
	@Positive
	private final Integer fieldLength;
	@NotNull
	@PositiveOrZero
	private final Integer fieldFraction;

	@JsonCreator
	public NewFieldDTO(@JsonProperty("extensionId") String extensionId,
					   @JsonProperty("fieldName") String fieldName,
					   @JsonProperty("fieldType") FieldType fieldType,
					   @JsonProperty("fieldLength") Integer fieldLength,
					   @JsonProperty(value = "fieldFraction", defaultValue = "0") Integer fieldFraction) {
		this.extensionId = extensionId;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLength = fieldLength;
		this.fieldFraction = fieldFraction;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		NewFieldDTO rhs = (NewFieldDTO) obj;
		return new EqualsBuilder()
				.append(getExtensionId(), rhs.getExtensionId())
				.append(getFieldName(), rhs.getFieldName())
				.append(getFieldType(), rhs.getFieldType())
				.append(getFieldLength(), rhs.getFieldLength())
				.append(getFieldFraction(), rhs.getFieldFraction())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getExtensionId())
				.append(getFieldName())
				.append(getFieldType())
				.append(getFieldLength())
				.append(getFieldFraction())
				.toHashCode();
	}

	public String getExtensionId() {
		return extensionId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public Integer getFieldLength() {
		return fieldLength;
	}

	public Integer getFieldFraction() {
		return fieldFraction;
	}
}
