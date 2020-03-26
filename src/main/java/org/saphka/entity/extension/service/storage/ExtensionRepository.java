package org.saphka.entity.extension.service.storage;

import org.saphka.entity.extension.model.Extension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExtensionRepository extends JpaRepository<Extension, UUID> {

    boolean existsByExtensionId(String extensionId);

    Optional<Extension> findFirstByExtensionId(String extensionId);

}
