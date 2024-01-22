package qh.springframework.factory.annotation.realize;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import qh.springframework.aop.proxy.core.InstantiationAwareBeanPostProcessor;
import qh.springframework.factory.annotation.core.Autowired;
import qh.springframework.factory.annotation.core.Qualifier;
import qh.springframework.factory.annotation.core.Value;
import qh.springframework.factory.bean.core.*;
import qh.springframework.factory.convert.core.ConversionService;
import qh.springframework.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if(valueAnnotation != null){
                Object value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue((String) value);

                Class<?> sourceType = value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getType(declaredField);
                if(!targetType.equals(sourceType)){
                    ConversionService conversionService = beanFactory.getConversionService();
                    if(conversionService != null){
                        if(conversionService.canConvert(sourceType, targetType)){
                            value = conversionService.convert(value, targetType);
                        }
                    }
                }

                BeanUtil.setFieldValue(bean, declaredField.getName(), value);
            }

        }


        for (Field declaredField : declaredFields) {
            Autowired autowiredAnnotation = declaredField.getAnnotation(Autowired.class);
            if(autowiredAnnotation != null){
                Class<?> type = declaredField.getType();
                String dependentBeanName = null;
                Object dependentBean = null;
                Qualifier qualifierAnnotation = declaredField.getAnnotation(Qualifier.class);
                if(qualifierAnnotation != null){
                    dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, type);
                }
                else{
                    dependentBean = beanFactory.getBean(type);
                }
                BeanUtil.setFieldValue(bean, declaredField.getName(), dependentBean);
            }
        }
        return pvs;


    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
