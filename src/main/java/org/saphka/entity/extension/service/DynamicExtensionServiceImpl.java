package org.saphka.entity.extension.service;

import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.*;

@Component
public class DynamicExtensionServiceImpl implements DynamicExtensionService, InitializingBean {

	private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader());
	private final Map<Class, Class> extensions = new HashMap<>();
	private final List<DynamicExtensionClassSource> classSources;

	@Autowired
	public DynamicExtensionServiceImpl(List<DynamicExtensionClassSource> classSources) {
		this.classSources = classSources;
	}

	@Override
	public ClassLoader getClassLoader() {
		return groovyClassLoader;
	}

	@Override
	public List<Class> getExtensionClasses() {
		return new ArrayList<>(extensions.values());
	}

	@Override
	public Optional<Class> findExtensionByInterface(Class<?> target) {
		return Optional.ofNullable(extensions.get(target));
	}

	@Override
	public boolean hasExtension(Class<?> target) {
		return extensions.containsKey(target);
	}

	@Override
	public void afterPropertiesSet() {
		extensions.putAll(classSources
				.stream()
				.flatMap((classSource) -> classSource.getClassesSourceCode().stream())
				.map(groovyClassLoader::parseClass)
				.map((loadedClass) -> Pair.of(
						loadedClass.getInterfaces()[0],
						loadedClass
				))
				.collect(Pair.toMap()));
	}
}
