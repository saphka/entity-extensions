package org.saphka.entity.extension.service.storage;

import org.saphka.entity.extension.model.ExtensionDTO;

import java.util.List;

/**
 * @author Alex Loginov
 * <p>
 * Read current configuration from Database
 */
public interface CurrentConfigurationReader {

    List<ExtensionDTO> getCurrentExtensions();

}
