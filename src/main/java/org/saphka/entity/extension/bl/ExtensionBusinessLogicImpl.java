package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.Extension;
import org.saphka.entity.extension.model.Field;
import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.FieldType;
import org.saphka.entity.extension.service.ApplicationRestarter;
import org.saphka.entity.extension.service.generator.KnowExtensionPointsProvider;
import org.saphka.entity.extension.service.storage.ExtensionRepository;
import org.saphka.entity.extension.service.storage.FieldRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExtensionBusinessLogicImpl implements ExtensionBusinessLogic {

	private final ObjectProvider<ApplicationRestarter> applicationRestarter;
	private final ExtensionRepository extensionRepository;
	private final FieldRepository fieldRepository;
	private final KnowExtensionPointsProvider knowExtensionPointsProvider;

	@Autowired
	public ExtensionBusinessLogicImpl(ObjectProvider<ApplicationRestarter> applicationRestarter, ExtensionRepository extensionRepository, FieldRepository fieldRepository, KnowExtensionPointsProvider knowExtensionPointsProvider) {
		this.applicationRestarter = applicationRestarter;
		this.extensionRepository = extensionRepository;
		this.fieldRepository = fieldRepository;
		this.knowExtensionPointsProvider = knowExtensionPointsProvider;
	}

	private void restartApplication() {
		applicationRestarter.ifAvailable(ApplicationRestarter::restart);
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

}
