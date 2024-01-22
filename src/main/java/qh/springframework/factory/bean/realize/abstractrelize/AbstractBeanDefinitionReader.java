package qh.springframework.factory.bean.realize.abstractrelize;

import qh.springframework.factory.io.DefaultResourceLoader;
import qh.springframework.factory.io.ResourceLoader;
import qh.springframework.factory.bean.core.BeanDefinitionReader;
import qh.springframework.factory.bean.core.BeanDefinitionRegistry;

public abstract  class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        this.registry = beanDefinitionRegistry;
        this.resourceLoader = resourceLoader;
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.resourceLoader = new DefaultResourceLoader();
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
