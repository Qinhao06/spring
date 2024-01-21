package qh.springframework.context.core;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
