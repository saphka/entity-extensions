package org.saphka.entity.extension.model;

import java.math.BigDecimal;

public enum FieldType {

    STRING(String.class), NUMBER(Long.class), DECIMAL(BigDecimal.class);

    private final String javaType;

    FieldType(Class clazz) {
        this.javaType = clazz.getCanonicalName();
    }

    public String getJavaType() {
        return javaType;
    }
}
