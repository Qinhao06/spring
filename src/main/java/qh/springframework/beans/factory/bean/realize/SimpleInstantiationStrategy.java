package qh.springframework.beans.factory.bean.realize;

import qh.springframework.beans.factory.bean.core.BeansException;
import qh.springframework.beans.factory.bean.core.InstantiationStrategy;
import qh.springframework.beans.factory.bean.core.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
        Class clazz =  beanDefinition.getBeanDefinitionClass();

        try {
            if(ctor != null){
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            }
            else {
                return clazz.getDeclaredConstructor().newInstance();
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new BeansException("Instantiation of bean failed, [" + clazz.getName() + "] ");
        }
    }
}
