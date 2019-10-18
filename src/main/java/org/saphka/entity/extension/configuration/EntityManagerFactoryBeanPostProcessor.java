package org.saphka.entity.extension.configuration;

import groovy.lang.GroovyClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

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
			mutableUnitInfo.addManagedClassName(GroovyConfiguration.packageName + "." + GroovyConfiguration.className);
		});
	}
}
