package org.saphka.entity.extension.service.storage;

import org.saphka.entity.extension.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Alex Loginov
 */
public interface FieldRepository extends JpaRepository<Field, UUID> {

    boolean existsByExtensionIdAndFieldName(String extensionId, String fieldName);

    List<Field> findByExtensionId(String extensionId);

}
