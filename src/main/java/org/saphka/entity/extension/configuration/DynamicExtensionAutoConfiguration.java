package org.saphka.entity.extension.configuration;

import org.saphka.entity.extension.service.DynamicExtensionClassService;
import org.saphka.entity.extension.service.DynamicExtensionClassServiceImpl;
import org.saphka.entity.extension.service.DynamicExtensionClassSource;
import org.saphka.entity.extension.service.generator.ExtensionClassGenerator;
import org.saphka.entity.extension.service.hibernate.DynamicExtensionEntityManagerFactoryBeanPostProcessor;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReaderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

public class DynamicExtensionAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DynamicExtensionClassService dynamicExtensionService(List<DynamicExtensionClassSource> classSources) {
		return new DynamicExtensionClassServiceImpl(classSources);
	}

	@Bean
	@ConditionalOnMissingBean
	public DynamicExtensionEntityManagerFactoryBeanPostProcessor dynamicExtensionEntityManagerFactoryBeanPostProcessor(DynamicExtensionClassService dynamicExtensionClassService) {
		return new DynamicExtensionEntityManagerFactoryBeanPostProcessor(dynamicExtensionClassService);
	}

	@Bean
	@ConditionalOnMissingBean
	@Order(Ordered.LOWEST_PRECEDENCE - 10)
	public ExtensionClassGenerator extensionClassGenerator(CurrentConfigurationReader reader) {
		return new ExtensionClassGenerator(reader);
	}

	@Bean
	@ConditionalOnMissingBean
	public CurrentConfigurationReader currentConfigurationReader(DataSource dataSource, JpaProperties jpaProperties) {
		return new CurrentConfigurationReaderImpl(dataSource, jpaProperties);
	}
}
