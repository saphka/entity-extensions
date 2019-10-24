package org.saphka.entity.extension.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.saphka.entity.extension"})
public class DynamicExtensionAutoConfiguration {

}
