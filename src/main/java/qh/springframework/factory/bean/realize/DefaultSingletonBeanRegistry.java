package qh.springframework.factory.bean.realize;

import qh.springframework.factory.bean.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private Map<String, Object> earlySingletonObjects = new HashMap<>();

    private Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    private Map<String, DisposalBeanAdapter> disposableBeanMap = new HashMap<>();

    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if(singletonObject == null){
            singletonObject = earlySingletonObjects.get(beanName);
            if(null == singletonObject){
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if(singletonFactory != null){
                    singletonObject = singletonFactory.getObject();
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }

            }
        }
        return singletonObject;
    }

    @Override
    public void registrySingleton(String beanName, Object bean) {
        singletonObjects.put(beanName, bean);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    public void destroySingletons() {
        List<String> collect = new ArrayList<>(disposableBeanMap.keySet());
        for (int i = collect.size() - 1; i >= 0 ; i--) {
            DisposableBean removeBean = disposableBeanMap.remove(collect.get(i));
            try {
                removeBean.destroy();
            } catch (BeansException e) {
                throw new BeansException("Destroy method on bean " + collect.get(i) + " threw an exception", e);
            }
        }
    }

    public void registerDisposableBean(String beanName, DisposalBeanAdapter bean) {
        if(!disposableBeanMap.containsKey(beanName)){
            disposableBeanMap.put(beanName, bean);
        }
    }


    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if(!singletonFactories.containsKey(beanName)){
            singletonFactories.put(beanName, singletonFactory);
            earlySingletonObjects.remove(beanName);
        }
    }

    protected Object getEarlySingleton(String beanName){
        return earlySingletonObjects.get(beanName);
    }

}
