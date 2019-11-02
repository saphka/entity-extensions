package org.saphka.entity.test;

import org.saphka.entity.extension.service.DynamicExtensionClassSource;

import java.util.Collections;
import java.util.List;

//@Component
public class DynamicExtensionClassSourceTestImpl implements DynamicExtensionClassSource {

	private final static String packageName = MyEntityExtension.class.getPackage().getName();
	private final static String className = "MyEntityExtensionGroovyImpl";
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


	@Override
	public List<String> getClassesSourceCode() {
		return Collections.singletonList(groovyText);
	}
}
