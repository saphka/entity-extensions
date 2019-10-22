package org.saphka.entity.extension;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.saphka.entity.extension.service.DynamicExtensionService;
import org.saphka.entity.extension.test.MyEntity;
import org.saphka.entity.extension.test.MyEntityExtension;
import org.saphka.entity.extension.test.MyEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@SpringBootTest(classes = {Application.class})
public class SimpleExtensionTest {

	@Configuration
	@EnableJpaRepositories(basePackages = {"org.saphka.entity.extension.test"})
	public static class TestConfiguration {
	}

	@Autowired
	private DynamicExtensionService extensionService;

	@Autowired
	private MyEntityRepository repository;

	@Autowired
	private EntityManager entityManager;

	@Test
	public void entityCreateTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> aClass = extensionService.findExtensionByInterface(MyEntityExtension.class).orElseThrow(() -> new IllegalArgumentException(""));

		HashMap<String, Object> data = new HashMap<>();
		data.put("first", "Foo");
		data.put("last", "Bar");

		MyEntityExtension extension = (MyEntityExtension) aClass.getDeclaredConstructor(Map.class).newInstance(data);

//		if (true) return;

		MyEntity entity = new MyEntity();
		entity.setId(1L);
		entity.setExtension(extension);

		repository.save(entity);

		List select_last_from_my_entitty = entityManager.createNativeQuery(
				"SELECT LAST, FIRST FROM PUBLIC.MY_ENTITY"
		).getResultList();
		assertThat(select_last_from_my_entitty).hasSize(1);

		Object firstResult = select_last_from_my_entitty.get(0);
		assertThat(firstResult).isInstanceOf(Object[].class);

		Object[] firstResultArray = (Object[]) firstResult;
		assertThat(firstResultArray[0]).isEqualTo("Bar");
		assertThat(firstResultArray[1]).isEqualTo("Foo");

	}

}
