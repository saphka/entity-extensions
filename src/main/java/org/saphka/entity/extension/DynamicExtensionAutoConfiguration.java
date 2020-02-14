package org.saphka.entity.extension;

import liquibase.integration.spring.SpringLiquibase;
import org.saphka.entity.extension.liquibase.ExtensionCapableSpringLiquibase;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * @author Alex Loginov
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class DynamicExtensionAutoConfiguration {

	@Configuration
	@EnableConfigurationProperties(LiquibaseProperties.class)
	@ConditionalOnProperty(prefix = "entity.extension.liquibase", name = "enabled", havingValue = "true", matchIfMissing = true)
	public static class DynamicExtensionLiquibaseConfiguration {

		private final LiquibaseProperties properties;

		@Autowired
		public DynamicExtensionLiquibaseConfiguration(LiquibaseProperties properties) {
			this.properties = properties;
		}

		@Bean
		public SpringLiquibase liquibase(DataSource dataSource,
										 @Value("${spring.liquibase.change-log}") String changeLog,
										 CurrentConfigurationReader currentConfigurationReader) {
			ExtensionCapableSpringLiquibase liquibase = new ExtensionCapableSpringLiquibase();

			liquibase.setChangeLog(this.properties.getChangeLog());
			liquibase.setContexts(this.properties.getContexts());
			liquibase.setDefaultSchema(this.properties.getDefaultSchema());
			liquibase.setLiquibaseSchema(this.properties.getLiquibaseSchema());
			liquibase.setLiquibaseTablespace(this.properties.getLiquibaseTablespace());
			liquibase.setDatabaseChangeLogTable(this.properties.getDatabaseChangeLogTable());
			liquibase.setDatabaseChangeLogLockTable(this.properties.getDatabaseChangeLogLockTable());
			liquibase.setDropFirst(this.properties.isDropFirst());
			liquibase.setShouldRun(this.properties.isEnabled());
			liquibase.setLabels(this.properties.getLabels());
			liquibase.setChangeLogParameters(this.properties.getParameters());
			liquibase.setRollbackFile(this.properties.getRollbackFile());
			liquibase.setTestRollbackOnUpdate(this.properties.isTestRollbackOnUpdate());

			liquibase.setDataSource(dataSource);
			liquibase.setExtensions(currentConfigurationReader.getCurrentExtensions());

			return liquibase;
		}
	}


}
