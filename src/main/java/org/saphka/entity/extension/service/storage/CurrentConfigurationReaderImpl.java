package org.saphka.entity.extension.service.storage;

import org.hibernate.dialect.*;
import org.hibernate.sql.SimpleSelect;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.saphka.entity.extension.configuration.DynamicExtensionSettings;
import org.saphka.entity.extension.model.ExtensionDTO;
import org.saphka.entity.extension.model.FieldDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnMissingBean(value = {CurrentConfigurationReader.class}, ignored = {CurrentConfigurationReaderImpl.class})
public class CurrentConfigurationReaderImpl implements CurrentConfigurationReader {

	private final static String[] EXTENSION_TABLE_COLUMNS = {"GUID", "EXTENSION_ID", "TABLE_NAME"};
	private final static String[] FIELD_TABLE_COLUMNS = {"GUID", "EXTENSION_ID", "FIELD_NAME", "FIELD_TYPE", "FIELD_LENGTH", "FIELD_FRACTION"};

	private final DataSource dataSource;
	private final JpaProperties jpaProperties;
	private final Dialect dialect;

	@Autowired
	public CurrentConfigurationReaderImpl(DataSource dataSource, JpaProperties jpaProperties) {
		this.dataSource = dataSource;
		this.jpaProperties = jpaProperties;
		this.dialect = constructDialect();
	}

	private Class<?> determineDatabaseDialect(Database database) {
		switch (database) {
			case DB2:
				return DB2Dialect.class;
			case DERBY:
				return DerbyTenSevenDialect.class;
			case H2:
				return H2Dialect.class;
			case HANA:
				return HANAColumnStoreDialect.class;
			case HSQL:
				return HSQLDialect.class;
			case INFORMIX:
				return InformixDialect.class;
			case MYSQL:
				return MySQL5Dialect.class;
			case ORACLE:
				return Oracle12cDialect.class;
			case POSTGRESQL:
				return PostgreSQL95Dialect.class;
			case SQL_SERVER:
				return SQLServer2012Dialect.class;
			case SYBASE:
				return SybaseDialect.class;
			default:
				throw new IllegalStateException("Unable to determine database driver class");
		}
	}

	private Dialect constructDialect() {
		Class databaseDialectClass;

		if (jpaProperties.getDatabasePlatform() != null) {
			try {
				databaseDialectClass = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(jpaProperties.getDatabasePlatform());
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		} else {
			databaseDialectClass = determineDatabaseDialect(jpaProperties.determineDatabase(dataSource));
		}

		try {
			return (Dialect) databaseDialectClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Cannot instantiate dialect", e);
		}
	}

	@Override
	public List<ExtensionDTO> getCurrentExtensions() {
		Map<String, String> extIdToTable = getCurrentExtensionsFromDB();
		Map<String, Set<FieldDTO>> extIdToField = getCurrentFieldsFromDB();

		return extIdToTable.entrySet().stream()
				.map((e) -> new ExtensionDTO(
						e.getKey(),
						e.getValue(),
						extIdToField.getOrDefault(e.getKey(), Collections.emptySet())
				))
				.collect(Collectors.toList());
	}

	private Map<String, String> getCurrentExtensionsFromDB() {
		SimpleSelect simpleSelect = new SimpleSelect(dialect);
		simpleSelect.setTableName(DynamicExtensionSettings.EXT_TABLE_NAME);
		for (String column : EXTENSION_TABLE_COLUMNS) {
			simpleSelect.addColumn(column);
		}

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			statement = connection.createStatement();

			resultSet = statement.executeQuery(simpleSelect.toStatementString());

			Map<String, String> result = new HashMap<>();

			while (resultSet.next()) {
				result.put(
						resultSet.getString(2),
						resultSet.getString(3)
				);
			}

			return result;

		} catch (SQLException ignored) {
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}

		return Collections.emptyMap();

	}

	private Map<String, Set<FieldDTO>> getCurrentFieldsFromDB() {
		SimpleSelect simpleSelect = new SimpleSelect(dialect);
		simpleSelect.setTableName(DynamicExtensionSettings.FIELD_TABLE_NAME);
		for (String column : FIELD_TABLE_COLUMNS) {
			simpleSelect.addColumn(column);
		}

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			statement = connection.createStatement();

			resultSet = statement.executeQuery(simpleSelect.toStatementString());

			Map<String, Set<FieldDTO>> result = new HashMap<>();

			while (resultSet.next()) {
				result
						.computeIfAbsent(resultSet.getString(2), (k) -> new HashSet<>())
						.add(new FieldDTO(
								UUIDTypeDescriptor.INSTANCE.wrap(resultSet.getObject(1), null),
								resultSet.getString(3),
								resultSet.getString(4),
								resultSet.getLong(5),
								resultSet.getLong(6)
						));

			}

			return result;
		} catch (SQLException ignored) {
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}

		return Collections.emptyMap();

	}
}
