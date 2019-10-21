package org.saphka.entity.extension.tuplizer;

import org.hibernate.mapping.Component;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.saphka.entity.extension.configuration.GroovyConfiguration;

import java.util.Arrays;

public class GroovyEntityTuplizer extends PojoComponentTuplizer {

	public GroovyEntityTuplizer(Component component) {
		super(component);
	}

	private Class findLoadedClass(Class targetInterface) {
		return Arrays.stream(GroovyConfiguration.getLoader().getLoadedClasses())
				.filter(targetInterface::isAssignableFrom)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No assignable class found for type " + targetInterface.getCanonicalName()));
	}

	@Override
	protected Instantiator buildInstantiator(Component component) {
		return new GroovyEntityInstantiator(component.getComponentClass());
	}
}
