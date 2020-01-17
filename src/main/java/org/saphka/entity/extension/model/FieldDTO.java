package org.saphka.entity.extension.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Alex Loginov
 */
public class FieldDTO {

	@NotNull
	private final UUID id;
	@Size(min = 1, max = 50)
	@NotNull
	private final String name;
	@Size(min = 1, max = 50)
	@NotNull
	private final FieldType type;
	@NotNull
	@PositiveOrZero
	private final Integer length;
	@NotNull
	@PositiveOrZero
	private final Integer fraction;

	@JsonCreator
	public FieldDTO(@JsonProperty("id") UUID id,
					@JsonProperty("name") String name,
					@JsonProperty("type") FieldType type,
					@JsonProperty("length") Integer length,
					@JsonProperty("fraction") Integer fraction) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.length = length;
		this.fraction = fraction;
	}

	public FieldDTO(Field field) {
		this(field.getId(), field.getFieldName(), field.getFieldType(), field.getFieldLength(), field.getFieldFraction());
	}

	public String getName() {
		return name;
	}

	public FieldType getType() {
		return type;
	}

	public Integer getLength() {
		return length;
	}

	public Integer getFraction() {
		return fraction;
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
		FieldDTO rhs = (FieldDTO) obj;
		return new EqualsBuilder()
				.append(id, rhs.id)
				.append(name, rhs.name)
				.append(type, rhs.type)
				.append(length, rhs.length)
				.append(fraction, rhs.fraction)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(type)
				.append(length)
				.append(fraction)
				.toHashCode();
	}

	public UUID getId() {
		return id;
	}
}
