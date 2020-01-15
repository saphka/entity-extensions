package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.ExtensionDTO;
import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.NewFieldDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ExtensionBusinessLogic {

	@Transactional
	FieldDTO createExtension(NewFieldDTO fieldDTO);

	List<ExtensionDTO> getRegisteredExtensions();

	Optional<ExtensionDTO> getRegisteredExtension(String extensionId);

}
