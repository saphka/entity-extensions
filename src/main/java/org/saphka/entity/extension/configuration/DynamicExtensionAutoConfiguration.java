package org.saphka.entity.extension.configuration;

import org.saphka.entity.extension.service.DynamicExtensionClassSource;
import org.saphka.entity.extension.service.DynamicExtensionService;
import org.saphka.entity.extension.service.DynamicExtensionServiceImpl;
import org.saphka.entity.extension.service.hibernate.DynamicExtensionEntityManagerFactoryBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class DynamicExtensionAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DynamicExtensionService dynamicExtensionService(List<DynamicExtensionClassSource> classSources) {
		return new DynamicExtensionServiceImpl(classSources);
	}

	@Bean
	@ConditionalOnMissingBean
	public DynamicExtensionEntityManagerFactoryBeanPostProcessor dynamicExtensionEntityManagerFactoryBeanPostProcessor(DynamicExtensionService dynamicExtensionService) {
		return new DynamicExtensionEntityManagerFactoryBeanPostProcessor(dynamicExtensionService);
	}
}
