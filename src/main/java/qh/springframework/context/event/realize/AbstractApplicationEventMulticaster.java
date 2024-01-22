package qh.springframework.context.event.realize;

import qh.springframework.factory.bean.core.BeanFactory;
import qh.springframework.factory.bean.core.BeanFactoryAware;
import qh.springframework.factory.bean.core.BeansException;
import qh.springframework.utils.ClassUtils;
import qh.springframework.context.event.core.ApplicationEvent;
import qh.springframework.context.event.core.ApplicationEventMulticaster;
import qh.springframework.context.event.core.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private BeanFactory beanFactory;

    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    protected List<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> listeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            if(supportEvent(event, applicationListener)){
                listeners.add(applicationListener);
            }
        }
        return listeners;
    }

    protected boolean supportEvent(ApplicationEvent event, ApplicationListener<ApplicationEvent> applicationListener) {
        Class<? extends ApplicationListener> alClass = applicationListener.getClass();

        Class<?> targetClass = ClassUtils.isCglibProxyClass(alClass) ? alClass.getSuperclass() : alClass;

        Type type = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> aleventClass;
        try {
            aleventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name " + className,e);
        }

        return aleventClass.isAssignableFrom(event.getClass());

    }


}
