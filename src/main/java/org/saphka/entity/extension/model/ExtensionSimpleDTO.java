package org.saphka.entity.extension.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ExtensionSimpleDTO {

	private final String extensionId;
	private final String tableName;


	public ExtensionSimpleDTO(String extensionId, String tableName) {
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
