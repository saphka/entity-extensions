package org.saphka.entity.extension.model;

import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;

/**
 * @author Alex Loginov
 */
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

	public String getJavaTypeWithConstraint(Integer length, Integer fraction) {
		String annotation = Strings.EMPTY;
		switch (this) {
			case STRING:
				annotation = "@javax.validation.constraints.Size(min = 1, max = " + length + ")";
				break;
			case NUMBER:
				break;
			case DECIMAL:
				annotation = "@javax.validation.constraints.Digits(integer = " + (length - fraction) + ", fraction = " + fraction + ")";
				break;
			default:
				throw new IllegalArgumentException("Unknown filed type " + this.name());
		}

		return annotation + " \n" + javaType;
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
