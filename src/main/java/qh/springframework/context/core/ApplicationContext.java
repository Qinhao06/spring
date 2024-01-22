package qh.springframework.context.core;

import qh.springframework.beans.core.io.ResourceLoader;
import qh.springframework.beans.factory.bean.core.HierarchicalBeanFactory;
import qh.springframework.beans.factory.bean.core.ListableBeanFactory;
import qh.springframework.context.event.core.ApplicationEventPublisher;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
