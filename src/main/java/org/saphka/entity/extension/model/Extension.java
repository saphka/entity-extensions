package org.saphka.entity.extension.model;

import org.saphka.entity.extension.configuration.DynamicExtensionSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = DynamicExtensionSettings.EXT_TABLE_NAME)
public class Extension {

	@Id
	@Column(name = "GUID")
	private UUID id;

	@Column(name = "EXTENSION_ID")
	private String extensionId;

	@Column(name = "TABLE_NAME")
	private String tableName;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
