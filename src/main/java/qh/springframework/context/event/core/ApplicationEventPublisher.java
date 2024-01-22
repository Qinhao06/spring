package qh.springframework.context.event.core;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
