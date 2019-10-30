package org.saphka.entity.extension.service.storage;

import org.saphka.entity.extension.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {

	boolean existsByExtensionIdAndFieldName(String extensionId, String fieldName);

}
