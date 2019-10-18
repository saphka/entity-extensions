package org.saphka.entity.extension.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
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
