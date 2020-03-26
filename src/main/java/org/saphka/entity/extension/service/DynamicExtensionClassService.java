package org.saphka.entity.extension.service;

import org.hibernate.service.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Alex Loginov
 */
public interface DynamicExtensionClassService extends Service {

    ClassLoader getClassLoader();

    List<Class<?>> getExtensionClasses();

    Optional<Class<?>> findExtensionByInterface(Class<?> target);

    boolean hasExtension(Class<?> target);

    <T> Optional<T> createExtensionClassByInterface(Class<T> target, Map<String, Object> properties);

}
