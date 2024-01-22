package qh.springframework.aop.proxy.core;

import qh.springframework.factory.bean.core.BeanPostProcessor;
import qh.springframework.factory.bean.core.BeansException;
import qh.springframework.factory.bean.core.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }


}
