package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.ExtensionDTO;
import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.FieldType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ExtensionBusinessLogic {

    @Transactional
    FieldDTO createExtension(String extensionId, String fieldName, FieldType fieldType, Long fieldLength, Long fieldFraction);

    List<ExtensionDTO> getRegisteredExtensions();

    Optional<ExtensionDTO> getRegisteredExtension(String extensionId);

}
