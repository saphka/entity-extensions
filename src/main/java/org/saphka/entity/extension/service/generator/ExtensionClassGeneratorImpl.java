package org.saphka.entity.extension.service.generator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.text.CaseUtils;
import org.saphka.entity.extension.annotation.DynamicExtensionTarget;
import org.saphka.entity.extension.annotation.EnableDynamicExtensions;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.saphka.entity.extension.service.storage.ExtensionDTO;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@ConditionalOnMissingBean(value = {ExtensionClassGenerator.class}, ignored = {ExtensionClassGeneratorImpl.class})
public class ExtensionClassGeneratorImpl implements ExtensionClassGenerator {

	private final static String classNamePostfix = "Impl";
	private final static String classHeader = "package " + ExtensionClassGeneratorImpl.class.getPackage().getName() + "\n" +
			"import groovy.transform.MapConstructor\n" +
			"import groovy.transform.Canonical\n" +
			"import javax.persistence.Column\n" +
			"@Canonical @MapConstructor " +
			"@javax.persistence.Embeddable " +
			"class ";

	private final CurrentConfigurationReader currentConfigurationReader;
	private final ConfigurableApplicationContext applicationContext;

	@Autowired
	public ExtensionClassGeneratorImpl(CurrentConfigurationReader currentConfigurationReader, ConfigurableApplicationContext applicationContext) {
		this.currentConfigurationReader = currentConfigurationReader;
		this.applicationContext = applicationContext;
	}

	@Override
	public List<String> getClassesSourceCode() {
		Map<String, ExtensionDTO> knownExtensionPointNames = findKnownExtensionPointNames();

		currentConfigurationReader.getCurrentExtensions().forEach((e) -> knownExtensionPointNames.put(e.getExtensionId(), e));

		return knownExtensionPointNames.values().stream().map(this::generateClassForExtension).collect(Collectors.toList());

	}

	private String generateClassForExtension(ExtensionDTO extensionDTO) {
		StringBuilder sb = new StringBuilder(classHeader);

		String[] parts = extensionDTO.getExtensionId().split("\\.");
		String simpleName = parts[parts.length - 1];

		sb.append(simpleName).append(classNamePostfix).append(" implements ").append(extensionDTO.getExtensionId());
		sb.append("{\n");
		extensionDTO.getFields().forEach((field) -> {
			sb.append("@Column(name=\"");
			sb.append(field.getName());
			sb.append("\")");
			sb.append(field.getType()).append(" ");
			sb.append(CaseUtils.toCamelCase(field.getName(), false, '_')).append("\n");
		});


		sb.append("}");

		return sb.toString();

	}

	private Map<String, ExtensionDTO> findKnownExtensionPointNames() {
		ClassLoader classLoader = Objects.requireNonNull(ClassUtils.getDefaultClassLoader());

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
			@Override
			protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
				AnnotationMetadata metadata = beanDefinition.getMetadata();
				return (metadata.isIndependent() && metadata.isInterface());
			}
		};
		scanner.addIncludeFilter(new AnnotationTypeFilter(DynamicExtensionTarget.class, true, true));

		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

		return beanFactory.getBeansWithAnnotation(EnableDynamicExtensions.class).values().stream()
				.map(Object::getClass)
				.map((clazz) -> {
					EnableDynamicExtensions annotation = AnnotationUtils.findAnnotation(clazz, EnableDynamicExtensions.class);
					return annotation != null ? Pair.of(clazz, annotation) : null;
				})
				.filter(Objects::nonNull)
				.map((p) -> {
					String[] basePackages = p.getSecond().basePackages();

					return !ArrayUtils.isEmpty(basePackages) ? basePackages : (new String[]{p.getFirst().getPackage().getName()});
				})
				.flatMap(Arrays::stream)
				.map(scanner::findCandidateComponents)
				.flatMap(Collection::stream)
				.map((bean) -> {
					try {
						return new ExtensionDTO(
								bean.getBeanClassName(),
								classLoader.loadClass(bean.getBeanClassName()).getAnnotation(DynamicExtensionTarget.class).tableName(),
								Collections.emptySet()

						);
					} catch (ClassNotFoundException e) {
						throw new IllegalArgumentException(e);
					}
				})
				.collect(Collectors.toMap(
						ExtensionDTO::getExtensionId,
						(e) -> e
				));
	}

}
