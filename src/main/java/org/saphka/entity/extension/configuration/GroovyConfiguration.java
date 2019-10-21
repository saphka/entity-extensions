package org.saphka.entity.extension.configuration;

import groovy.lang.GroovyClassLoader;
import org.saphka.entity.extension.entity.MyEntityExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

@Configuration
public class GroovyConfiguration {

	public final static String packageName = MyEntityExtension.class.getPackage().getName();
	public final static String className = "MyEntityExtensionGroovyImpl";
	private final static String groovyText = "" +
			"package " + packageName + "\n" +
			"import groovy.transform.MapConstructor\n" +
			"import groovy.transform.Canonical\n" +
			"import javax.persistence.Column\n" +
			"@Canonical @MapConstructor " +
//			"@javax.persistence.Entity " +
			"@javax.persistence.Embeddable " +
			"class " + className + " implements " + MyEntityExtension.class.getCanonicalName() + "{\n" +
			"     @Column String first\n" +
			"	  @Column String last\n" +
			"     @Column Integer age\n" +
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
