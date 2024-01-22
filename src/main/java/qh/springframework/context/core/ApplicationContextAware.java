package qh.springframework.context.core;

import qh.springframework.beans.factory.bean.core.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}
