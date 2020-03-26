package org.saphka.entity.test;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.saphka.entity.extension.service.DynamicExtensionClassService;
import org.saphka.entity.extension.service.DynamicExtensionClassSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@SpringBootTest(classes = {Application.class, SimpleExtensionTest.TestConfiguration.class})
public class SimpleExtensionTest {

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public static class TestConfiguration {

        //trick to init table before class loading
        @Bean
        public DynamicExtensionClassSource getDataSource(DataSource dataSource) throws IOException {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = dataSource.getConnection();

                statement = connection.createStatement();

                statement.executeUpdate(
                        IOUtils.toString(new ClassPathResource("test/schema-h2.sql").getInputStream(), Charset.defaultCharset())
                );
                statement.executeUpdate(
                        IOUtils.toString(new ClassPathResource("test/data-h2.sql").getInputStream(), Charset.defaultCharset())
                );
                connection.commit();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Cannot insert data to config tables", e);
            } finally {
                JdbcUtils.closeStatement(statement);
                JdbcUtils.closeConnection(connection);
            }

            return Collections::emptyList;
        }

    }

    @Autowired
    private DynamicExtensionClassService extensionService;

    @Autowired
    private MyEntityRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void entityCreateTest() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("first", "Foo");
        data.put("last", "Bar");
        data.put("age", "11");
        data.put("bad", "property");

        MyEntityExtension extension = extensionService.createExtensionClassByInterface(MyEntityExtension.class, data).orElseThrow(IllegalArgumentException::new);

        MyEntityExtensionEmpty extensionEmpty = extensionService.createExtensionClassByInterface(MyEntityExtensionEmpty.class, Collections.emptyMap()).orElseThrow(IllegalArgumentException::new);

//		if (true) return;

        assertThat(extension.getProperty("last")).isEqualTo("Bar");

        extension.setProperty("last", "Baz");

        MyEntity entity = new MyEntity();
        entity.setId(1L);
        entity.setExtension(extension);
        entity.setEmpty(extensionEmpty);

        MyEntity myEntitySaveResult = repository.save(entity);

        List<Object[]> select_last_from_my_entitty = entityManager.createNativeQuery(
                "SELECT LAST, FIRST, AGE FROM PUBLIC.MY_ENTITY"
        ).getResultList();
        assertThat(select_last_from_my_entitty).hasSize(1);

        Object firstResult = select_last_from_my_entitty.get(0);
        assertThat(firstResult).isInstanceOf(Object[].class);

        Object[] firstResultArray = (Object[]) firstResult;
        assertThat(firstResultArray[0]).isEqualTo("Baz");
        assertThat(firstResultArray[1]).isEqualTo("Foo");
        assertThat(firstResultArray[2]).isEqualTo(new BigInteger("11"));

        MyEntityExtension myEntityExtensionSaveResult = myEntitySaveResult.getExtension();
        Map<String, Object> propertiesMap = myEntityExtensionSaveResult.getPropertiesMap();

        assertThat(propertiesMap).containsEntry("last", "Baz");
        assertThat(propertiesMap).containsEntry("first", "Foo");

    }

}
