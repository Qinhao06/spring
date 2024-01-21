package qh.springframework.context.core;

import qh.springframework.beans.core.io.ResourceLoader;
import qh.springframework.beans.factory.core.HierarchicalBeanFactory;
import qh.springframework.beans.factory.core.ListableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
