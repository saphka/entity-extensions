package org.saphka.entity.extension.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
				annotation = "@" + javax.validation.constraints.Size.class.getCanonicalName() + "(min = 1, max = " + length + ")";
				break;
			case NUMBER:
				break;
			case DECIMAL:
				annotation = "@" + javax.validation.constraints.Digits.class.getCanonicalName() + "(integer = " + (length - fraction) + ", fraction = " + fraction + ")";
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

	public FieldConfig getConfig() {
		switch (this) {
			case STRING:
				return new FieldConfig(true, false);
			case NUMBER:
				return new FieldConfig(false, false);
			case DECIMAL:
				return new FieldConfig(true, true);
		}

		throw new IllegalArgumentException("Unknown filed type " + this.name());
	}

	public static class FieldConfig {
		private final Boolean needsLength;
		private final Boolean needsFraction;

		@JsonCreator
		public FieldConfig(@JsonProperty("needsLength") Boolean needsLength,
						   @JsonProperty("needsFraction") Boolean needsFraction) {
			this.needsLength = needsLength;
			this.needsFraction = needsFraction;
		}

		public Boolean getNeedsLength() {
			return needsLength;
		}

		public Boolean getNeedsFraction() {
			return needsFraction;
		}
	}
}
