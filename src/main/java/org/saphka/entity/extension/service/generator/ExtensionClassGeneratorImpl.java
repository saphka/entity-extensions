package org.saphka.entity.extension.service.generator;

import org.apache.commons.text.CaseUtils;
import org.saphka.entity.extension.service.storage.CurrentConfigurationReader;
import org.saphka.entity.extension.model.ExtensionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

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
	private final KnowExtensionPointsProvider knowExtensionPointsProvider;

	@Autowired
	public ExtensionClassGeneratorImpl(CurrentConfigurationReader currentConfigurationReader, KnowExtensionPointsProvider knowExtensionPointsProvider) {
		this.currentConfigurationReader = currentConfigurationReader;
		this.knowExtensionPointsProvider = knowExtensionPointsProvider;
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
		return knowExtensionPointsProvider.getKnownExtensionPoints().entrySet().stream()
				.map((e) ->
						Pair.of(
								e.getKey(),
								new ExtensionDTO(
										e.getValue().getExtensionId(),
										e.getValue().getTableName(),
										Collections.emptySet()
								)
						)
				)
				.collect(Pair.toMap());
	}

}
