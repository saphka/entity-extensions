package org.saphka.entity.extension.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.Set;

public class ExtensionDTO {

    private final String extensionId;
    private final String tableName;
    private final Set<FieldDTO> fields;


    public ExtensionDTO(String extensionId, String tableName, Set<FieldDTO> fields) {
        this.extensionId = extensionId;
        this.tableName = tableName;
        this.fields = fields;
    }

    public ExtensionDTO(Extension extension, Set<FieldDTO> fields) {
        this(extension.getExtensionId(), extension.getTableName(), fields);
    }

    public String getExtensionId() {
        return extensionId;
    }

    public Set<FieldDTO> getFields() {
        return Collections.unmodifiableSet(fields);
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
        ExtensionDTO rhs = (ExtensionDTO) obj;
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
