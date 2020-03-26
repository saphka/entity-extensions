package org.saphka.entity.extension.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Alex Loginov
 */
public class ExtensionSimpleDTO {

	@Size(min = 1, max = 200)
	@NotNull
	private final String extensionId;
	@Size(min = 1, max = 100)
	@NotNull
	private final String tableName;

	@JsonCreator
	public ExtensionSimpleDTO(@JsonProperty("extensionId") String extensionId, @JsonProperty("tableName") String tableName) {
		this.extensionId = extensionId;
		this.tableName = tableName;
	}

	public ExtensionSimpleDTO(Extension extension) {
		this(extension.getExtensionId(), extension.getTableName());
	}

	public String getExtensionId() {
		return extensionId;
	}


	public String getTableName() {
		return tableName;
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
		ExtensionSimpleDTO rhs = (ExtensionSimpleDTO) obj;
		return new EqualsBuilder()
				.append(extensionId, rhs.extensionId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(extensionId)
				.toHashCode();
	}
}
