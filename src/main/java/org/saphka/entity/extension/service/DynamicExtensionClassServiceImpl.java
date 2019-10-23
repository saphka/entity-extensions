package org.saphka.entity.extension.service;

import groovy.lang.GroovyClassLoader;
import org.saphka.entity.extension.annotation.DynamicExtensionTarget;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.util.Pair;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DynamicExtensionClassServiceImpl implements DynamicExtensionClassService, InitializingBean {

	private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader());
	private final Map<Class, Class> extensions = new HashMap<>();
	private final List<DynamicExtensionClassSource> classSources;

	public DynamicExtensionClassServiceImpl(List<DynamicExtensionClassSource> classSources) {
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
						detectExtensionInterface(loadedClass),
						loadedClass
				))
				.collect(Collectors.toMap(
						Pair::getFirst,
						Pair::getSecond,
						(o, n) -> n //new values always override
				)));
	}

	private Class detectExtensionInterface(Class clazz) {
		return Arrays.stream(clazz.getInterfaces())
				.filter((intf) -> intf.isAnnotationPresent(DynamicExtensionTarget.class))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Class " + clazz.getCanonicalName() + " must implement at least one interface marked with org.saphka.entity.extension.annotation.DynamicExtensionTarget"));
	}
}
