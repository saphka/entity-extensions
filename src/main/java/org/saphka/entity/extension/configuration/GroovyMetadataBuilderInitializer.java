package org.saphka.entity.extension.configuration;

import groovy.lang.GroovyClassLoader;
import org.hibernate.annotations.common.reflection.AnnotationReader;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.boot.spi.MetadataBuilderImplementor;
import org.hibernate.boot.spi.MetadataBuilderInitializer;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.cfg.annotations.reflection.JPAMetadataProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

public class GroovyMetadataBuilderInitializer implements MetadataBuilderInitializer {

	@Override
	public void contribute(MetadataBuilder metadataBuilder, StandardServiceRegistry serviceRegistry) {
		MetadataBuilderImplementor builderImplementor = (MetadataBuilderImplementor) metadataBuilder;

		MetadataProviderInjector metadataProviderInjector = (MetadataProviderInjector) builderImplementor.getBootstrapContext().getReflectionManager();

		metadataProviderInjector.setMetadataProvider(new GroovyAwareMetadataProvider(builderImplementor.getBootstrapContext()));

	}

	private static class GroovyAwareMetadataProvider extends JPAMetadataProvider {

		private final Map<AnnotatedElement, AnnotationReader> cache = new HashMap<AnnotatedElement, AnnotationReader>(100);
		private final GroovyClassLoader groovyClassLoader = GroovyConfiguration.getLoader();

		GroovyAwareMetadataProvider(BootstrapContext bootstrapContext) {
			super(bootstrapContext);
		}

		@Override
		public AnnotationReader getAnnotationReader(AnnotatedElement annotatedElement) {
			if (isGroovyClass(annotatedElement)) {
				return cache.computeIfAbsent(annotatedElement, (e) -> new GroovyAnnotationReader(e, groovyClassLoader));
			} else {
				return super.getAnnotationReader(annotatedElement);
			}
		}

		private boolean isGroovyClass(AnnotatedElement annotatedElement) {
			return false;
		}
	}

	private static class GroovyAnnotationReader implements AnnotationReader {

		private final AnnotatedElement annotatedElement;
		private final GroovyClassLoader groovyClassLoader;

		private GroovyAnnotationReader(AnnotatedElement annotatedElement, GroovyClassLoader groovyClassLoader) {
			this.annotatedElement = annotatedElement;
			this.groovyClassLoader = groovyClassLoader;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
			return annotatedElement.getAnnotation(annotationType);
		}

		@Override
		public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationType) {
			return annotatedElement.isAnnotationPresent(annotationType);
		}

		@Override
		public Annotation[] getAnnotations() {
			return annotatedElement.getAnnotations();
		}
	}
}
