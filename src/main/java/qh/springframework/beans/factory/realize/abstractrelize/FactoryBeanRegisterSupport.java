package qh.springframework.beans.factory.realize.abstractrelize;

import qh.springframework.beans.factory.core.BeansException;
import qh.springframework.beans.factory.core.FactoryBean;
import qh.springframework.beans.factory.realize.DefaultSingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegisterSupport extends DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeansCache = new ConcurrentHashMap<>();

    protected Object getCachedBeanForFactoryBean(String beanName) {
        Object object = this.factoryBeansCache.get(beanName);
        return object;
    }

    protected Object getBeanFromFactoryBean(FactoryBean factoryBean, String beanName) {
        Object cachedBean = null;
        if(factoryBean.isSingleton()){
            cachedBean = getCachedBeanForFactoryBean(beanName);
            if(cachedBean == null){
                cachedBean = doGetBeanFromFactoryBean(factoryBean, beanName);
                factoryBeansCache.put(beanName, cachedBean);
            }

        }else {
            cachedBean =  doGetBeanFromFactoryBean(factoryBean, beanName);
        }
        return cachedBean;

    }

    private Object doGetBeanFromFactoryBean(FactoryBean factoryBean, String beanName) {
        try {
            return factoryBean.getObject();
        }catch (Exception e) {
            throw new BeansException("factoryBean [" + factoryBean + "] throws an Exception from getObject", e);
        }

    }


}
