package qh.springframework.context.realize;

import qh.springframework.beans.factory.core.BeanFactory;
import qh.springframework.context.core.ApplicationEvent;
import qh.springframework.context.core.ApplicationListener;
import qh.springframework.context.realize.abstractrealize.AbstractApplicationEventMulticaster;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {


    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener applicationListener : getApplicationListeners(event)) {
            applicationListener.onApplicationEvent(event);
        }


    }
}
