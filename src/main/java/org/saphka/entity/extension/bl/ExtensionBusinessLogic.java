package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.FieldType;
import org.springframework.transaction.annotation.Transactional;

public interface ExtensionBusinessLogic {

	@Transactional
	FieldDTO createExtension(String extensionId, String fieldName, FieldType fieldType, Long fieldLength, Long fieldFraction);

}
