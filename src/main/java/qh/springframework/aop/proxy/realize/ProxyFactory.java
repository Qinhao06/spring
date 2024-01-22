package qh.springframework.aop.proxy.realize;

import qh.springframework.aop.advice.core.AdvisedSupport;
import qh.springframework.aop.proxy.core.AopProxy;

public class ProxyFactory implements AopProxy {

    private final AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }


    @Override
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if(advisedSupport.isProxyTargetClass()){
            return new JdkDynamicAopProxy(advisedSupport);
        }
        return new Cglib2AopProxy(advisedSupport);
    }

}
