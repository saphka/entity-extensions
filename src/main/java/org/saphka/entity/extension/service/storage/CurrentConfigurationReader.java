package org.saphka.entity.extension.service.storage;

import org.saphka.entity.extension.model.ExtensionDTO;

import java.util.List;

public interface CurrentConfigurationReader {

    List<ExtensionDTO> getCurrentExtensions();

}
