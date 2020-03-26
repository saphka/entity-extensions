package org.saphka.entity.extension.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class FieldDTO {

    private final UUID id;
    private final String name;
    private final FieldType type;
    private final Long length;
    private final Long fraction;

    public FieldDTO(UUID id, String name, FieldType type, Long length, Long fraction) {
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
