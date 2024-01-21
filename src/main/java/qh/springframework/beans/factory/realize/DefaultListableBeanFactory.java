package qh.springframework.beans.factory.realize;

import qh.springframework.beans.factory.core.*;
import qh.springframework.beans.factory.realize.abstractrelize.AbstractAutowiredCapableBeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowiredCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {


    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();


    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null){
            throw new BeansException("no bean names ' " + beanName + " ' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiate() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public Boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition)->{
            Class beanDefinitionClass = beanDefinition.getBeanDefinitionClass();
            if(type.isAssignableFrom(beanDefinitionClass))
                result.put(beanName, (T) getBean(beanName));

        });
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        beanDefinitionMap.forEach((beanName, beanDefinition)->{
            Class beanDefinitionClass = beanDefinition.getBeanDefinitionClass();
            if(requiredType.isAssignableFrom(beanDefinitionClass))
                beanNames.add(beanName);
        });
        if(beanNames.size() == 1){
            return (T) getBean(beanNames.get(0));
        }else if(beanNames.size() > 1){
            throw new BeansException("more than one bean of type " + requiredType + " is defined");
        }else{
            throw new BeansException("no bean of type " + requiredType + " is defined");
        }
    }
}
