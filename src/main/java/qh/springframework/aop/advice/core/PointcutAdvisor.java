package qh.springframework.aop.advice.core;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
