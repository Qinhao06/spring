package qh.springframework.context.core;

import qh.springframework.beans.factory.bean.core.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();


}
