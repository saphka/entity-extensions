package org.saphka.entity.extension.service.generator;

import org.apache.commons.lang3.ArrayUtils;
import org.saphka.entity.extension.annotation.DynamicExtensionTarget;
import org.saphka.entity.extension.annotation.EnableDynamicExtensions;
import org.saphka.entity.extension.model.ExtensionSimpleDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alex Loginov
 */
@Component
public class KnowExtensionPointsProviderImpl implements KnowExtensionPointsProvider, InitializingBean {

    private final ConfigurableApplicationContext applicationContext;
    private final Map<String, ExtensionSimpleDTO> knownPoints = new HashMap<>();

    @Autowired
    public KnowExtensionPointsProviderImpl(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, ExtensionSimpleDTO> getKnownExtensionPoints() {
        return Collections.unmodifiableMap(knownPoints);
    }

    private Map<String, ExtensionSimpleDTO> initKnownPoints() {
        ClassLoader classLoader = Objects.requireNonNull(ClassUtils.getDefaultClassLoader());

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                AnnotationMetadata metadata = beanDefinition.getMetadata();
                return (metadata.isIndependent() && metadata.isInterface());
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(DynamicExtensionTarget.class, true, true));

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        return beanFactory.getBeansWithAnnotation(EnableDynamicExtensions.class).values().stream()
                .map(Object::getClass)
                .map((clazz) -> {
                    EnableDynamicExtensions annotation = AnnotationUtils.findAnnotation(clazz, EnableDynamicExtensions.class);
                    return annotation != null ? Pair.of(clazz, annotation) : null;
                })
                .filter(Objects::nonNull)
                .map((p) -> {
                    String[] basePackages = p.getSecond().basePackages();

                    return !ArrayUtils.isEmpty(basePackages) ? basePackages : (new String[]{p.getFirst().getPackage().getName()});
                })
                .flatMap(Arrays::stream)
                .map(scanner::findCandidateComponents)
                .flatMap(Collection::stream)
                .map((bean) -> {
                    try {
                        return new ExtensionSimpleDTO(
                                bean.getBeanClassName(),
                                classLoader.loadClass(bean.getBeanClassName()).getAnnotation(DynamicExtensionTarget.class).tableName()

                        );
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                })
                .collect(Collectors.toMap(
                        ExtensionSimpleDTO::getExtensionId,
                        (e) -> e
                ));
    }

    @Override
    public void afterPropertiesSet() {
        knownPoints.putAll(initKnownPoints());
    }
}
