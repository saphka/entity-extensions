create table CONFIG_EXTENSION_DATA
(
    GUID           varbinary(16) primary key,
    EXTENSION_ID   nvarchar(200) not null,
    TABLE_NAME     nvarchar(100) not null,
    FIELD_NAME     nvarchar(50)  not null,
    FIELD_TYPE     nvarchar(50)  not null,
    FIELD_LENGTH   long          not null,
    FIELD_FRACTION long          not null,
);
