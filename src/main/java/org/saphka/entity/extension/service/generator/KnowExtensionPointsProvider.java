package org.saphka.entity.extension.service.generator;

import org.saphka.entity.extension.model.ExtensionSimpleDTO;

import java.util.Map;

/**
 * @author Alex Loginov
 * <p>
 * Scans packages for all entities that have field interface marked with {@link org.saphka.entity.extension.annotation.DynamicExtensionTarget}
 */
public interface KnowExtensionPointsProvider {

    Map<String, ExtensionSimpleDTO> getKnownExtensionPoints();
}
