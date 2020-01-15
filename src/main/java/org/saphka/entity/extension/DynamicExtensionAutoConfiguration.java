package org.saphka.entity.extension;

import liquibase.integration.spring.SpringLiquibase;
import org.saphka.entity.extension.liquibase.ExtensionCapableSpringLiquibase;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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

	@Bean
	@ConditionalOnProperty(name = "entity.extension.liquibase.enabled", havingValue = "true", matchIfMissing = true)
	public SpringLiquibase liquibase(DataSource dataSource, @Value("${spring.liquibase.change-log}") String changeLog, CurrentConfigurationReader currentConfigurationReader) {
		ExtensionCapableSpringLiquibase liquibase = new ExtensionCapableSpringLiquibase();
		liquibase.setChangeLog(changeLog);
		liquibase.setDataSource(dataSource);
		liquibase.setExtensions(currentConfigurationReader.getCurrentExtensions());

		return liquibase;
	}

}
