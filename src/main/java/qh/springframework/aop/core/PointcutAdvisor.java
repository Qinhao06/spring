package qh.springframework.aop.core;

public interface PointcutAdvisor extends Advisor{

    Pointcut getPointcut();

}
