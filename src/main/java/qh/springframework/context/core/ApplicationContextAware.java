package qh.springframework.context.core;

import qh.springframework.common.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);

}
