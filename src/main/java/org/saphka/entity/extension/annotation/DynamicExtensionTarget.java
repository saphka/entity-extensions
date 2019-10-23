package org.saphka.entity.extension.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface DynamicExtensionTarget {

	String tableName();

}
