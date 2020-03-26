package org.saphka.entity.test;

import org.saphka.entity.extension.annotation.DynamicExtensionTarget;
import org.saphka.entity.extension.api.ExtensionPropertyAccess;

@DynamicExtensionTarget(tableName = "MY_ENTITY")
public interface MyEntityExtensionEmpty extends ExtensionPropertyAccess {
}
