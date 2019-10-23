package org.saphka.entity.extension.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicExtensionTarget {

	String tableName();

}
