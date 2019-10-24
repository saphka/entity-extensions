package org.saphka.entity.extension.service.storage;

import org.hibernate.dialect.*;
import org.hibernate.sql.SimpleSelect;
import org.saphka.entity.extension.configuration.DynamicExtensionSettings;
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

	private final static String[] EXTENSION_TABLE_COLUMNS = {"EXTENSION_ID", "TABLE_NAME", "FIELD_NAME", "FIELD_TYPE", "FIELD_LENGTH", "FIELD_FRACTION"};

	private final DataSource dataSource;
	private final JpaProperties jpaProperties;

	@Autowired
	public CurrentConfigurationReaderImpl(DataSource dataSource, JpaProperties jpaProperties) {
		this.dataSource = dataSource;
		this.jpaProperties = jpaProperties;
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
		Dialect dialect = constructDialect();

		SimpleSelect simpleSelect = new SimpleSelect(dialect);
		simpleSelect.setTableName(DynamicExtensionSettings.TABLE_NAME);
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

			Map<String, String> extIdToTable = new HashMap<>();
			Map<String, Set<FieldDTO>> extIdToField = new HashMap<>();

			while (resultSet.next()) {
				extIdToField
						.computeIfAbsent(resultSet.getString(1), (k) -> new HashSet<>())
						.add(new FieldDTO(
								resultSet.getString(3),
								resultSet.getString(4),
								resultSet.getLong(5),
								resultSet.getLong(6)
						));
				extIdToTable.put(
						resultSet.getString(1),
						resultSet.getString(2)
				);
			}

			return extIdToTable.entrySet().stream()
					.map((e) -> new ExtensionDTO(
							e.getKey(),
							e.getValue(),
							extIdToField.getOrDefault(e.getKey(), Collections.emptySet())
					))
					.collect(Collectors.toList());

		} catch (SQLException ignored) {
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}

		return Collections.emptyList();

	}
}
