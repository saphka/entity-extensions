package org.saphka.entity.extension.annotation;

import org.saphka.entity.extension.DynamicExtensionAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Alex Loginov
 * <p>
 * Enables extension framework auto-configuration
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DynamicExtensionAutoConfiguration.class})
public @interface EnableDynamicExtensions {

	String[] basePackages() default {};
}
