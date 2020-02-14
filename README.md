# Entity Extensions Framework
Simple framework that uses spring boot and hibernate to create dynamic extensions and manipulate them via REST API.

Liquibase is used to refactor DB and create new fields.
Consider adding `classpath:/db/changelog/db.changelog-extension-config-tables.xml` to your current liquibase changelog to create necessary tables for the framework.

## Start-up

Build this library with maven and connect dependency to your project
```xml
<dependencies>
    <dependency>
		<groupId>org.saphka</groupId>
		<artifactId>entity-extensions</artifactId>
        <version>1.0.0-SNAPSHOT</version>
	</dependency>
</dependencies>
``` 

Activate framework auto-configuration with `org.saphka.entity.extension.annotation.EnableDynamicExtensions`

Use `@org.saphka.entity.extension.annotation.DynamicExtensionTarget` to mark your interfaces in your entities as possible extension points.
For example
```java
@DynamicExtensionTarget(tableName = "MY_ENTITY")
public interface MyEntityExtension extends ExtensionPropertyAccess {

}
```

```java
@Entity
@Table(name = "MY_ENTITY")
public class MyEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private MyEntityExtension extension;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MyEntityExtension getExtension() {
		return extension;
	}

	public void setExtension(MyEntityExtension extension) {
		this.extension = extension;
	}

}
```

Use `org.saphka.entity.extension.api.ExtensionPropertyAccess` for convenient access to extension properties.

When application start the framework scans your base packages for extension targets and puts them in its cache.
You can use an API exposed with `org.saphka.entity.extension.controller.DynamicExtensionsController` to create new fields in existing entities.

Framework does not support extension modifications so be careful.

## Authors

* **Alexander Loginov** - *Initial work* - [saphka](https://github.com/saphka)

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details
