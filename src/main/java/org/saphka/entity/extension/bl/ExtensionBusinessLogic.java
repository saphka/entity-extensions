package org.saphka.entity.extension.bl;

import org.saphka.entity.extension.model.ExtensionDTO;
import org.saphka.entity.extension.model.FieldConfigDTO;
import org.saphka.entity.extension.model.FieldDTO;
import org.saphka.entity.extension.model.NewFieldDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Alex Loginov
 * <p>
 * Extension framework Business logic for API
 */

public interface ExtensionBusinessLogic {

    /**
     * Create new extension field.
     * Client should restart the application for changes to take effect
     *
     * @param fieldDTO field data
     * @return created field
     */
    @Transactional
    FieldDTO createExtension(NewFieldDTO fieldDTO);

    /**
     * Get all registered extensions
     *
     * @return {@link List} of extensions with fields
     */
    List<ExtensionDTO> getRegisteredExtensions();

    /**
     * Get extension by id
     *
     * @param extensionId extension id
     * @return extension info with all fields
     */
    Optional<ExtensionDTO> getRegisteredExtension(String extensionId);

    /**
     * List of types to use in API
     *
     * @return {@link List} of {@link org.saphka.entity.extension.model.FieldType} mapped to name
     */
    List<FieldConfigDTO> getPossibleFieldTypes();

}
