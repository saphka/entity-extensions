package org.saphka.entity.extension.service.storage;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FieldDTO {

	private final String name;
	private final String type;
	private final Long length;
	private final Long fraction;

	public FieldDTO(String name, String type, Long length, Long fraction) {
		this.name = name;
		this.type = type;
		this.length = length;
		this.fraction = fraction;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Long getLength() {
		return length;
	}

	public Long getFraction() {
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
				.append(name, rhs.name)
				.append(type, rhs.type)
				.append(length, rhs.length)
				.append(fraction, rhs.fraction)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.append(type)
				.append(length)
				.append(fraction)
				.toHashCode();
	}
}
