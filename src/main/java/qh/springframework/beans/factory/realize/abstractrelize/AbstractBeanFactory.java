package qh.springframework.beans.factory.realize.abstractrelize;

import qh.springframework.beans.factory.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于实现 getBean 方法
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegisterSupport implements ConfigurableBeanFactory {

    List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private List<StringValueResolver> stringValueResolvers = new ArrayList<>();


    @Override
    public Object getBean(String beanName) throws BeansException{
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        return doGetBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
        return (T)getBean(beanName);
    }


    protected <T> T doGetBean(final String beanName, final Object[] args){
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            return (T)getObjectForBeanInstance(sharedInstance, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object bean = createBean(beanName, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean, beanName);
    }

    private Object getObjectForBeanInstance(Object bean, String beanName) {
        if(!(bean instanceof FactoryBean)){
            return bean;
        }

        Object cachedBeanForFactoryBean = getCachedBeanForFactoryBean(beanName);

        if(cachedBeanForFactoryBean == null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) bean;
            cachedBeanForFactoryBean = getBeanFromFactoryBean(factoryBean, beanName);
        }
        return cachedBeanForFactoryBean;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    public abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver resolver) {
        stringValueResolvers.add(resolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : stringValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return  result;
    }
}
