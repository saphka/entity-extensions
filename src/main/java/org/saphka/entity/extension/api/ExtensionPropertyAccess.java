package org.saphka.entity.extension.api;

import java.util.Map;

public interface ExtensionPropertyAccess {

	Object getProperty(String property);

	void setProperty(String property, Object value);

	Map<String, Object> getPropertiesMap();

}
