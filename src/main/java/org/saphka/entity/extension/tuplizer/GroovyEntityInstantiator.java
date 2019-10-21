package org.saphka.entity.extension.tuplizer;

import org.hibernate.tuple.Instantiator;
import org.saphka.entity.extension.configuration.GroovyConfiguration;

import java.io.Serializable;
import java.util.Arrays;

public class GroovyEntityInstantiator implements Instantiator {

	private final Class targetClass;

	public GroovyEntityInstantiator(Class targetInterface) {
		this.targetClass = findLoadedClass(targetInterface);
	}

	private Class findLoadedClass(Class targetInterface) {
		return Arrays.stream(GroovyConfiguration.getLoader().getLoadedClasses())
				.filter(targetInterface::isAssignableFrom)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No assignable class found for type " + targetInterface.getCanonicalName()));
	}

	@Override
	public Object instantiate(Serializable id) {
		return instantiate();
	}

	@Override
	public Object instantiate() {
		try {
			return targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Could not instantiate class " + targetClass.getCanonicalName(), e);
		}
	}

	@Override
	public boolean isInstance(Object object) {
		return targetClass.isInstance(object);
	}
}
