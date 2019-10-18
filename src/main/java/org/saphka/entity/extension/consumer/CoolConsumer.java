package org.saphka.entity.extension.consumer;

import org.saphka.entity.extension.configuration.GroovyConfiguration;
import org.saphka.entity.extension.entity.MyEntity;
import org.saphka.entity.extension.entity.MyEntityExtension;
import org.saphka.entity.extension.entity.MyEntityRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import groovy.lang.GroovyClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CoolConsumer implements InitializingBean {

	private final GroovyClassLoader groovyClassLoader;
	private final MyEntityRepository repository;

	@Autowired
	public CoolConsumer(GroovyClassLoader groovyClassLoader, MyEntityRepository repository) {
		this.groovyClassLoader = groovyClassLoader;
		this.repository = repository;
	}

	@Override
	public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

		Class<?> aClass = groovyClassLoader.loadClass(GroovyConfiguration.packageName + "." + GroovyConfiguration.className);

		Map<String, Object> data = new HashMap<>();
		data.put("first", "Foo");
		data.put("last", "Bar");

		MyEntityExtension extension = (MyEntityExtension) aClass.getDeclaredConstructor(Map.class).newInstance(data);

//		if (true) return;

		MyEntity entity = new MyEntity();
		entity.setId(1L);
		entity.setExtension(extension);

		repository.save(entity);

	}
}
