package org.saphka.entity.extension.api;

public interface ExtensionPropertyAccess {

	Object getProperty(String property);

	void setProperty(String property, Object value);

}
