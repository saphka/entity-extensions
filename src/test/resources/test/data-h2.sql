INSERT INTO CONFIG_EXTENSION_POINT
    (GUID, EXTENSION_ID, TABLE_NAME)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'MY_ENTITY');
INSERT INTO CONFIG_EXTENSION_FIELD
(GUID, EXTENSION_ID, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'FIRST', 'java.lang.String',
        40, 0);
INSERT INTO CONFIG_EXTENSION_FIELD
(GUID, EXTENSION_ID, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'LAST', 'java.lang.String',
        40, 0);
INSERT INTO CONFIG_EXTENSION_FIELD
(GUID, EXTENSION_ID, FIELD_NAME, FIELD_TYPE, FIELD_LENGTH, FIELD_FRACTION)
values (random_uuid(), 'org.saphka.entity.extension.test.MyEntityExtension', 'AGE', 'java.lang.Integer',
        0, 0);