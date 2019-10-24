INSERT INTO CONFIG_EXTENSION_DATA
(GUID, EXTENSION_ID, TABLE_NAME, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'MY_ENTITY', 'FIRST', 'java.lang.String',
        40, 0);
INSERT INTO CONFIG_EXTENSION_DATA
(GUID, EXTENSION_ID, TABLE_NAME, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'MY_ENTITY', 'LAST', 'java.lang.String',
        40, 0);
INSERT INTO CONFIG_EXTENSION_DATA
(GUID, EXTENSION_ID, TABLE_NAME, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'MY_ENTITY', 'AGE', 'java.lang.Integer',
        0, 0);