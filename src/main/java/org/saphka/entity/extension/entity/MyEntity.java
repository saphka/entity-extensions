package org.saphka.entity.extension.entity;

import org.hibernate.annotations.Tuplizer;
import org.saphka.entity.extension.tuplizer.GroovyEntityTuplizer;

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
	@Tuplizer(impl = GroovyEntityTuplizer.class)
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
