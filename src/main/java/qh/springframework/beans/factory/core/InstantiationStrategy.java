package qh.springframework.beans.factory.core;

import java.lang.reflect.Constructor;

/**
 * Bean 创建策略
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;

}
