package org.saphka.entity.extension.configuration;

import groovy.lang.GroovyClassLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

@Configuration
public class GroovyConfiguration {

	public final static String packageName = "org.saphka.entity.extension";
	public final static String className = "MyEntityExtensionGroovyImpl";
	private final static String groovyText = "" +
			"package " + packageName + "\n" +
			"import groovy.transform.MapConstructor\n" +
			"import groovy.transform.Canonical\n" +
			"import javax.persistence.Embeddable\n" +
			"@Embeddable @Canonical @MapConstructor class " + className + " implements org.saphka.test.entity.MyEntityExtension {\n" +
			"     String first, last\n" +
			"     int age\n" +
			" }";

	private final static GroovyClassLoader loader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader());

	public static GroovyClassLoader getLoader() {
		return loader;
	}

	@Bean
	public GroovyClassLoader groovyClassLoader() {
		loader.parseClass(groovyText);
		return loader;
	}

}
