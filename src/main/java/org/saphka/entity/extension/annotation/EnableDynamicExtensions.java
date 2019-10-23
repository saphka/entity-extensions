package org.saphka.entity.extension.annotation;

import org.saphka.entity.extension.configuration.DynamicExtensionAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DynamicExtensionAutoConfiguration.class})
public @interface EnableDynamicExtensions {
}
