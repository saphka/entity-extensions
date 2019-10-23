package org.saphka.entity.extension.service.hibernate;

import org.hibernate.annotations.Target;
import org.hibernate.annotations.common.reflection.AnnotationReader;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.boot.spi.MetadataBuilderImplementor;
import org.hibernate.boot.spi.MetadataBuilderInitializer;
import org.hibernate.cfg.annotations.reflection.JPAMetadataProvider;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.saphka.entity.extension.configuration.DynamicExtensionSettings;
import org.saphka.entity.extension.service.DynamicExtensionService;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicExtensionMetadataBuilderInitializer implements MetadataBuilderInitializer {

	@Override
	public void contribute(MetadataBuilder metadataBuilder, StandardServiceRegistry serviceRegistry) {
		ConfigurationService configurationService = serviceRegistry.getService(ConfigurationService.class);

		DynamicExtensionService extensionService = configurationService.getSetting(DynamicExtensionSettings.DYNAMIC_SERVICE, DynamicExtensionService.class, null);
		//Don't contribute if extension service is not configured
		if (extensionService == null) {
			return;
		}

		MetadataBuilderImplementor builderImplementor = (MetadataBuilderImplementor) metadataBuilder;

		@SuppressWarnings("deprecation")
		MetadataProviderInjector metadataProviderInjector = (MetadataProviderInjector) builderImplementor.getBootstrapContext().getReflectionManager();

		metadataProviderInjector.setMetadataProvider(new DynamicExtensionAwareMetadataProvider(builderImplementor.getBootstrapContext(), extensionService));

	}

	private static class DynamicExtensionAwareMetadataProvider extends JPAMetadataProvider {

		private final Map<AnnotatedElement, AnnotationReader> cache = new HashMap<>(100);
		private final DynamicExtensionService extensionService;

		DynamicExtensionAwareMetadataProvider(BootstrapContext bootstrapContext, DynamicExtensionService extensionService) {
			super(bootstrapContext);
			this.extensionService = extensionService;
		}

		@Override
		public AnnotationReader getAnnotationReader(AnnotatedElement annotatedElement) {
			Class returnClass = checkHasExtension(annotatedElement);
			if (returnClass != null) {
				return cache.computeIfAbsent(annotatedElement, (e) -> new DynamicExtensionAnnotationReader(e, returnClass, extensionService));
			} else {
				return super.getAnnotationReader(annotatedElement);
			}
		}

		private Class checkHasExtension(AnnotatedElement annotatedElement) {
			Class returnClass;
			if (annotatedElement instanceof Field) {
				Field field = (Field) annotatedElement;
				returnClass = field.getType();
			} else if (annotatedElement instanceof Method) {
				Method method = (Method) annotatedElement;
				returnClass = method.getReturnType();
			} else {
				return null;
			}

			return extensionService.hasExtension(returnClass) ? returnClass : null;
		}
	}

	private static class DynamicExtensionAnnotationReader implements AnnotationReader {

		private final AnnotatedElement annotatedElement;
		private final Class returnClass;
		private final DynamicExtensionService extensionService;
		private Target synthesizedAnnotation;

		private DynamicExtensionAnnotationReader(AnnotatedElement annotatedElement, Class returnClass, DynamicExtensionService extensionService) {
			this.annotatedElement = annotatedElement;
			this.returnClass = returnClass;
			this.extensionService = extensionService;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
			if (Target.class.equals(annotationType)) {
				return annotationType.cast(createTargetAnnotation());
			}

			return annotatedElement.getAnnotation(annotationType);
		}

		@Override
		public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationType) {
			if (Target.class.equals(annotationType)) {
				return true;
			}
			return annotatedElement.isAnnotationPresent(annotationType);
		}

		@Override
		public Annotation[] getAnnotations() {
			Annotation[] initialAnnotations = annotatedElement.getAnnotations();
			Annotation[] annotations = new Annotation[initialAnnotations.length + 1];
			System.arraycopy(initialAnnotations, 0, annotations, 0, initialAnnotations.length);
			annotations[annotations.length - 1] = createTargetAnnotation();

			return annotations;
		}

		private synchronized Target createTargetAnnotation() {
			if (synthesizedAnnotation == null) {
				Map<String, Object> attributes = new HashMap<>();
				attributes.put("value", extensionService.findExtensionByInterface(returnClass).orElseThrow(() -> new IllegalArgumentException("No extension found for class " + returnClass.getCanonicalName())));
				synthesizedAnnotation = AnnotationUtils.synthesizeAnnotation(attributes, Target.class, annotatedElement);
			}

			return synthesizedAnnotation;
		}
	}
}
