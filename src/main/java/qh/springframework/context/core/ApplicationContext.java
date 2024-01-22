package qh.springframework.context.core;

import qh.springframework.factory.io.ResourceLoader;
import qh.springframework.factory.bean.core.HierarchicalBeanFactory;
import qh.springframework.factory.bean.core.ListableBeanFactory;
import qh.springframework.context.event.core.ApplicationEventPublisher;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
