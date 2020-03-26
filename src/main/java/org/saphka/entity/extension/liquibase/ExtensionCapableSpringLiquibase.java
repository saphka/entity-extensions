package org.saphka.entity.extension.liquibase;

import liquibase.Liquibase;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.saphka.entity.extension.model.ExtensionDTO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Alex Loginov
 * <p>
 * Spirng Liquiibase compilant bean than can accept extensions as part of the change-set
 */

public class ExtensionCapableSpringLiquibase extends SpringLiquibase {

    private final static String AUTHOR_NAME = ExtensionCapableSpringLiquibase.class.getCanonicalName();

    private List<ExtensionDTO> extensions = new ArrayList<>();

    public ExtensionCapableSpringLiquibase() {
        super();
    }

    public List<ExtensionDTO> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<ExtensionDTO> extensions) {
        this.extensions = extensions;
    }

    @Override
    protected Liquibase createLiquibase(Connection c) throws LiquibaseException {
        Liquibase liquibase = super.createLiquibase(c);

        DatabaseChangeLog databaseChangeLog = liquibase.getDatabaseChangeLog();

        extensions.stream()
                .filter(ext -> !ext.getFields().isEmpty())
                .map(ext -> ext.getFields()
                        .stream()
                        .map(field -> {
                            ChangeSet changeSet = new ChangeSet(
                                    field.getId().toString(),
                                    AUTHOR_NAME,
                                    false,
                                    false,
                                    databaseChangeLog.getFilePath(),
                                    null,
                                    null,
                                    databaseChangeLog
                            );

                            AddColumnChange addColumn = new AddColumnChange();
                            addColumn.setTableName(ext.getTableName());

                            AddColumnConfig columnConfig = new AddColumnConfig();
                            columnConfig
                                    .setName(field.getName())
                                    .setType(field.getType().getLiquibaseType(
                                            field.getLength(),
                                            field.getFraction()
                                    ));

                            addColumn.addColumn(columnConfig);
                            changeSet.addChange(addColumn);

                            return changeSet;
                        }))
                .flatMap(Function.identity())
                .forEach(databaseChangeLog::addChangeSet);

        return liquibase;
    }
}
