package qh.springframework.beans.factory.core;

import qh.springframework.common.Aware;

public interface BeanClassLoaderAware extends Aware {

    void setBeanClassLoader(ClassLoader classLoader);

}

