package org.saphka.entity.extension.annotation;

import java.lang.annotation.*;

/**
 * @author Alex Loginov
 *
 * Use this annotation on interface to mark it as an extension point and then include it in some JPA entity
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicExtensionTarget {

	String tableName();

}
