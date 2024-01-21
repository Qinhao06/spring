package qh.springframework.beans.factory.core;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    Boolean containsBeanDefinition(String beanName);

}
