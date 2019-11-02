package org.saphka.entity.test;

import javax.persistence.*;

@Entity
@Table(name = "MY_ENTITY")
public class MyEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private MyEntityExtension extension;

	@Embedded
	private MyEntityExtensionEmpty empty;

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

	public MyEntityExtensionEmpty getEmpty() {
		return empty;
	}

	public void setEmpty(MyEntityExtensionEmpty empty) {
		this.empty = empty;
	}
}
