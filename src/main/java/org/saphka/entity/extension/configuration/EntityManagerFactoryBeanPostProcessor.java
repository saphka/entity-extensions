package org.saphka.entity.extension.configuration;

import groovy.lang.GroovyClassLoader;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class EntityManagerFactoryBeanPostProcessor implements BeanPostProcessor {

	private final GroovyClassLoader groovyClassLoader;

	@Autowired
	public EntityManagerFactoryBeanPostProcessor(GroovyClassLoader groovyClassLoader) {
		this.groovyClassLoader = groovyClassLoader;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof LocalContainerEntityManagerFactoryBean) {
			postProcess((LocalContainerEntityManagerFactoryBean) bean);
		}
		return bean;
	}

	private void postProcess(LocalContainerEntityManagerFactoryBean bean) {
		bean.setPersistenceUnitPostProcessors((mutableUnitInfo) -> {
			mutableUnitInfo.getProperties().put(
					AvailableSettings.LOADED_CLASSES,
					Arrays.asList(groovyClassLoader.getLoadedClasses())

			);
			mutableUnitInfo.getProperties().put(
					AvailableSettings.CLASSLOADERS,
					Collections.singletonList(groovyClassLoader)
			);
			mutableUnitInfo.addManagedClassName(GroovyConfiguration.packageName + "." + GroovyConfiguration.className);
		});
	}
}
