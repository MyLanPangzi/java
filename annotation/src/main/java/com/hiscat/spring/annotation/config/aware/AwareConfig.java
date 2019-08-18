package com.hiscat.spring.annotation.config.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.util.StringValueResolver;

/**
 * @author Administrator
 */
@Configuration
//@Data
public class AwareConfig implements ApplicationContextAware, ApplicationEventPublisherAware,
        BeanClassLoaderAware, BeanFactoryAware, BeanNameAware, EmbeddedValueResolverAware, EnvironmentAware,
        ImportAware,MessageSourceAware, NotificationPublisherAware,ResourceLoaderAware {
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private String name;
    private ApplicationContext applicationContext;
    private ApplicationEventPublisher applicationEventPublisher;
    private StringValueResolver stringValueResolver;
    private Environment environment;
    private MessageSource messageSource;
    private ResourceLoader resourceLoader;
    private AnnotationMetadata annotationMetadata;
    private NotificationPublisher notificationPublisher;

    @Override
    public String toString() {
        return "AwareConfig{" +
                "\nclassLoader=" + classLoader +
                "\n beanFactory=" + beanFactory +
                "\n name='" + name + '\'' +
                "\n applicationContext=" + applicationContext +
                "\n applicationEventPublisher=" + applicationEventPublisher +
                "\n stringValueResolver=" + stringValueResolver +
                "\n environment=" + environment +
                "\n messageSource=" + messageSource +
                "\n resourceLoader=" + resourceLoader +
                "\n annotationMetadata=" + annotationMetadata +
                "\n notificationPublisher=" + notificationPublisher +
                '}';
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

    }

    @Override
    public void setBeanName(String name) {
        this.name = name;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
        System.out.println(resolver.resolveStringValue("Hello ${os.name} "));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationMetadata = importMetadata;

    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;

    }

}
