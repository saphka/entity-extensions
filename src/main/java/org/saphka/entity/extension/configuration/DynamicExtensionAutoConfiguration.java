package org.saphka.entity.extension.configuration;

import org.saphka.entity.extension.service.DynamicExtensionClassSource;
import org.saphka.entity.extension.service.DynamicExtensionClassService;
import org.saphka.entity.extension.service.DynamicExtensionClassServiceImpl;
import org.saphka.entity.extension.service.hibernate.DynamicExtensionEntityManagerFactoryBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
}
