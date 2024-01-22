package qh.springframework.context.event.realize;

import qh.springframework.beans.factory.bean.core.BeanFactory;
import qh.springframework.context.event.core.ApplicationEvent;
import qh.springframework.context.event.core.ApplicationListener;
import qh.springframework.context.event.realize.AbstractApplicationEventMulticaster;

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
