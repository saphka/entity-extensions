package org.saphka.entity.extension.annotation;

import java.lang.annotation.*;

/**
 * @author Alex Loginov
 * <p>
 * Use this annotation on interface to mark it as an extension point and then include it in some JPA entity
 * @see org.saphka.entity.extension.api.ExtensionPropertyAccess for easy assecc to extension properties
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicExtensionTarget {

	String tableName();

}
