package org.saphka.entity.extension.service.generator;

import org.saphka.entity.extension.model.ExtensionSimpleDTO;

import java.util.Map;

public interface KnowExtensionPointsProvider {

	Map<String, ExtensionSimpleDTO> getKnownExtensionPoints();
}
