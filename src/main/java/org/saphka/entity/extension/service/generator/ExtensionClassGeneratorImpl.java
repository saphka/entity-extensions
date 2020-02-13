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

/**
 * @author Alex Loginov
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@ConditionalOnMissingBean(value = {ExtensionClassGenerator.class}, ignored = {ExtensionClassGeneratorImpl.class})
public class ExtensionClassGeneratorImpl implements ExtensionClassGenerator {

	private final static String classNamePostfix = "Impl";
	private final static String classHeader =
			"package " + ExtensionClassGeneratorImpl.class.getPackage().getName() + "\n" +
					"@" + groovy.transform.MapConstructor.class.getCanonicalName() + "\n" +
					"@" + groovy.transform.Canonical.class.getCanonicalName() + "\n" +
					"@" + groovy.transform.ToString.class.getCanonicalName() + "\n" +
					"@" + groovy.transform.EqualsAndHashCode.class.getCanonicalName() + "\n" +
					"@" + javax.persistence.Embeddable.class.getCanonicalName() + "\n" +
					"class ";

	private final static String getPropertiesMapMethod = "" +
			"public java.util.Map getPropertiesMap() {\n" +
			"  this.class.declaredFields.findAll { !it.synthetic }.collectEntries {\n" +
			"    [ (it.name):this.\"$it.name\" ]\n" +
			"  }\n" +
			"}";

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

		sb.append(simpleName)
				.append(classNamePostfix)
				.append(" implements ")
				.append(extensionDTO.getExtensionId())
				.append("{\n");
		extensionDTO.getFields().forEach((field) ->
				sb.append("@")
						.append(javax.persistence.Column.class.getCanonicalName())
						.append("(name=\"")
						.append(field.getName())
						.append("\")")
						.append(field.getType().getJavaTypeWithConstraint(field.getLength(), field.getFraction()))
						.append(" ")
						.append(CaseUtils.toCamelCase(field.getName(), false, '_'))
						.append("\n")
		);

		sb.append(getPropertiesMapMethod);

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
