package qh.springframework.aop.realize;

import qh.springframework.aop.core.AdvisedSupport;
import qh.springframework.aop.core.AopProxy;

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
