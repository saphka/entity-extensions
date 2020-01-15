package org.saphka.entity.extension.model;

import java.math.BigDecimal;

public enum FieldType {

	STRING(String.class, "java.sql.Types.NVARCHAR"),
	NUMBER(Long.class, "java.sql.Types.BIGINT"),
	DECIMAL(BigDecimal.class, "java.sql.Types.DECIMAL");

	private final String javaType;
	private final String sqlType;

	FieldType(Class<?> clazz, String sqlType) {
		this.javaType = clazz.getCanonicalName();
		this.sqlType = sqlType;
	}

	public String getJavaType() {
		return javaType;
	}

	public String getLiquibaseType(Integer length, Integer fraction) {
		switch (this) {
			case STRING:
				return sqlType + "(" + length + ")";
			case NUMBER:
				return sqlType;
			case DECIMAL:
				return sqlType + "(" + length + "," + fraction + ")";
		}

		throw new IllegalArgumentException("Unknown filed type " + this.name());
	}
}
