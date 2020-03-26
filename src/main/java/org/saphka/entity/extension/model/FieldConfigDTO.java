package org.saphka.entity.extension.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.saphka.entity.extension.model.FieldType.FieldConfig;

public class FieldConfigDTO {

    private final FieldType type;
    private final FieldConfig config;

    @JsonCreator
    public FieldConfigDTO(@JsonProperty("type") FieldType type, @JsonProperty("config") FieldConfig config) {
        this.type = type;
        this.config = config;
    }

    public FieldType getType() {
        return type;
    }

    public FieldConfig getConfig() {
        return config;
    }
}
