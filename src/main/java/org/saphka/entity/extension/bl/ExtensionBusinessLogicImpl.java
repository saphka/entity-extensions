package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.*;
import org.saphka.entity.extension.service.generator.KnowExtensionPointsProvider;
import org.saphka.entity.extension.service.storage.ExtensionRepository;
import org.saphka.entity.extension.service.storage.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExtensionBusinessLogicImpl implements ExtensionBusinessLogic {

	private final ExtensionRepository extensionRepository;
	private final FieldRepository fieldRepository;
	private final KnowExtensionPointsProvider knowExtensionPointsProvider;

	@Autowired
	public ExtensionBusinessLogicImpl(ExtensionRepository extensionRepository, FieldRepository fieldRepository, KnowExtensionPointsProvider knowExtensionPointsProvider) {
		this.extensionRepository = extensionRepository;
		this.fieldRepository = fieldRepository;
		this.knowExtensionPointsProvider = knowExtensionPointsProvider;
	}

	@Override
	public FieldDTO createExtension(String extensionId, String fieldName, FieldType fieldType, Long fieldLength, Long fieldFraction) {
		if (fieldRepository.existsByExtensionIdAndFieldName(extensionId, fieldName)) {
			throw new IllegalArgumentException("Fields with extension id " + extensionId + " and field name " + fieldName + " already exists");
		}

		if (!extensionRepository.existsByExtensionId(extensionId)) {
			if (!knowExtensionPointsProvider.getKnownExtensionPoints().containsKey(extensionId)) {
				throw new IllegalArgumentException("Unknown extension id " + extensionId);
			}

			Extension extension = new Extension();
			extension.setId(UUID.randomUUID());
			extension.setExtensionId(extensionId);
			extension.setTableName(knowExtensionPointsProvider.getKnownExtensionPoints().get(extensionId).getTableName());

			extensionRepository.save(extension);
		}

		Field field = new Field();
		field.setId(UUID.randomUUID());
		field.setExtensionId(extensionId);
		field.setFieldName(fieldName);
		field.setFieldType(fieldType);
		field.setFieldLength(fieldLength);
		field.setFieldFraction(fieldFraction);

		return new FieldDTO(fieldRepository.save(field));
	}

	@Override
	public List<ExtensionDTO> getRegisteredExtensions() {
		Map<String, List<Field>> fieldMap = fieldRepository.findAll().stream().collect(Collectors.toMap(
				Field::getExtensionId,
				field -> {
					List<Field> fields = new ArrayList<>(1);
					fields.add(field);
					return fields;
				},
				(oldList, newList) -> {
					List<Field> finalList = new ArrayList<>(oldList.size() + newList.size());
					finalList.addAll(oldList);
					finalList.addAll(newList);
					return finalList;
				}
		));

		return extensionRepository.findAll().stream().map(
				extension -> new ExtensionDTO(
						extension,
						fieldMap.getOrDefault(extension.getExtensionId(), Collections.emptyList()).stream()
								.map(FieldDTO::new).collect(Collectors.toSet())
				)
		).collect(Collectors.toList());
	}

	@Override
	public Optional<ExtensionDTO> getRegisteredExtension(String extensionId) {
		return extensionRepository.findFirstByExtensionId(extensionId).map(
				extension -> new ExtensionDTO(
						extension,
						fieldRepository.findByExtensionId(extensionId).stream()
								.map(FieldDTO::new).collect(Collectors.toSet())
				)
		);
	}

}
