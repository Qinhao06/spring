package qh.springframework.beans.factory.realize;

import net.sf.cglib.proxy.NoOp;
import qh.springframework.beans.factory.core.BeansException;
import qh.springframework.beans.factory.core.BeanDefinition;

import java.lang.reflect.Constructor;

import net.sf.cglib.proxy.Enhancer;
import qh.springframework.beans.factory.core.InstantiationStrategy;

public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanDefinitionClass());
        enhancer.setCallback(new NoOp() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if( ctor == null){
            return enhancer.create();
        }
        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
