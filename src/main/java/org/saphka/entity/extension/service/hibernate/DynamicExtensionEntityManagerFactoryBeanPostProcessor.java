package org.saphka.entity.extension.service.hibernate;

import org.hibernate.cfg.AvailableSettings;
import org.saphka.entity.extension.configuration.DynamicExtensionSettings;
import org.saphka.entity.extension.service.DynamicExtensionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

@Component
public class DynamicExtensionEntityManagerFactoryBeanPostProcessor implements BeanPostProcessor {

	private final DynamicExtensionService dynamicExtensionService;

	@Autowired
	public DynamicExtensionEntityManagerFactoryBeanPostProcessor(DynamicExtensionService dynamicExtensionService) {
		this.dynamicExtensionService = dynamicExtensionService;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof LocalContainerEntityManagerFactoryBean) {
			postProcess((LocalContainerEntityManagerFactoryBean) bean);
		}
		return bean;
	}

	private void postProcess(LocalContainerEntityManagerFactoryBean bean) {
		bean.getJpaPropertyMap().put(
				AvailableSettings.CLASSLOADERS,
				Collections.singletonList(dynamicExtensionService.getClassLoader())
		);

		bean.setPersistenceUnitPostProcessors((mutableUnitInfo) -> {
			Properties properties = mutableUnitInfo.getProperties();
			properties.put(
					AvailableSettings.LOADED_CLASSES,
					dynamicExtensionService.getExtensionClasses()
			);
			properties.put(
					DynamicExtensionSettings.DYNAMIC_SERVICE,
					dynamicExtensionService
			);

			dynamicExtensionService.getExtensionClasses().stream().map(
					Class::getCanonicalName
			).forEach(mutableUnitInfo::addManagedClassName);
		});
	}
}
