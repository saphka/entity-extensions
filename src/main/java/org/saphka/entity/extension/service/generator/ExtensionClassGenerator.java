package org.saphka.entity.extension.service.generator;

import org.saphka.entity.extension.annotation.DynamicExtensionTarget;
import org.saphka.entity.extension.annotation.EnableDynamicExtensions;
import org.saphka.entity.extension.configuration.DynamicExtensionAutoConfiguration;
import org.saphka.entity.extension.service.DynamicExtensionClassSource;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.saphka.entity.extension.service.storage.ExtensionDTO;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ExtensionClassGenerator implements DynamicExtensionClassSource {

	private final static String classNamePostfix = "Impl";
	private final static String classHeader = "package " + ExtensionClassGenerator.class.getPackage().getName() + "\n" +
			"import groovy.transform.MapConstructor\n" +
			"import groovy.transform.Canonical\n" +
			"import javax.persistence.Column\n" +
			"@Canonical @MapConstructor " +
			"@javax.persistence.Embeddable " +
			"class ";

	private final CurrentConfigurationReader currentConfigurationReader;

	public ExtensionClassGenerator(CurrentConfigurationReader currentConfigurationReader) {
		this.currentConfigurationReader = currentConfigurationReader;
	}


	@Override
	public List<String> getClassesSourceCode() {
		Map<String, ExtensionDTO> knownExtensionPointNames = findKnownExtensionPointNames();

		currentConfigurationReader.getCurrentExtensions().forEach((e) -> knownExtensionPointNames.put(e.getExtensionId(), e));

		return knownExtensionPointNames.values().stream().map(this::generateClassForExtension).collect(Collectors.toList());
	}

	private String generateClassForExtension(ExtensionDTO extensionDTO) {
		StringBuilder sb = new StringBuilder(classHeader);

		String[] parts = extensionDTO.getExtensionId().split(".");
		String simpleName = parts[parts.length - 1];

		sb.append(simpleName).append(classNamePostfix).append(" implements ").append(extensionDTO.getExtensionId());
		sb.append("{\n");
		extensionDTO.getFields().forEach((field) -> {
			sb.append("@Column ");
			sb.append(field.getType()).append(" ");
			sb.append(field.getName()).append("\n");
		});
		sb.append("}");

		return sb.toString();

	}

	private Map<String, ExtensionDTO> findKnownExtensionPointNames() {
		String[] basePackages = (String[]) new StandardAnnotationMetadata(DynamicExtensionAutoConfiguration.class)
				.getAnnotationAttributes(EnableDynamicExtensions.class.getName())
				.get("basePackages");

		ClassLoader classLoader = Objects.requireNonNull(ClassUtils.getDefaultClassLoader());

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(DynamicExtensionTarget.class));

		return Arrays.stream(basePackages)
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
