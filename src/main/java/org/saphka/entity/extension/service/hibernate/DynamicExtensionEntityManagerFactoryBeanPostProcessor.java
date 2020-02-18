package org.saphka.entity.extension.service.hibernate;

import org.hibernate.cfg.AvailableSettings;
import org.saphka.entity.extension.configuration.DynamicExtensionSettings;
import org.saphka.entity.extension.service.DynamicExtensionClassService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Alex Loginov
 */
@Component
public class DynamicExtensionEntityManagerFactoryBeanPostProcessor implements BeanPostProcessor {

	private final DynamicExtensionClassService dynamicExtensionClassService;

	@Autowired
	public DynamicExtensionEntityManagerFactoryBeanPostProcessor(DynamicExtensionClassService dynamicExtensionClassService) {
		this.dynamicExtensionClassService = dynamicExtensionClassService;
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
				Collections.singletonList(dynamicExtensionClassService.getClassLoader())
		);

		bean.setPersistenceUnitPostProcessors((mutableUnitInfo) -> {
			Properties properties = mutableUnitInfo.getProperties();
			properties.put(
					AvailableSettings.LOADED_CLASSES,
					dynamicExtensionClassService.getExtensionClasses()
			);
			properties.put(
					DynamicExtensionSettings.DYNAMIC_SERVICE,
					dynamicExtensionClassService
			);

			dynamicExtensionClassService.getExtensionClasses().stream().map(
					Class::getCanonicalName
			).forEach(mutableUnitInfo::addManagedClassName);
		});
	}
}
