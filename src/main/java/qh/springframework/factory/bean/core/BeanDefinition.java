package qh.springframework.factory.bean.core;

public class BeanDefinition {

    private Class beanDefinitionClass;

    private PropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private boolean singleton = true;

    private boolean prototype = false;

    private String scope = ConfigurableBeanFactory.SCOPE_SINGLETON;

    public BeanDefinition(Class beanDefinitionClass) {
        this(beanDefinitionClass, null);
    }

    public BeanDefinition(Class beanDefinitionClass, PropertyValues propertyValues) {
        this.beanDefinitionClass = beanDefinitionClass;
        this.propertyValues = propertyValues == null ? new PropertyValues() : propertyValues;
    }

    public Class getBeanDefinitionClass() {
        return beanDefinitionClass;
    }

    public void setBeanDefinitionClass(Class beanDefinitionClass) {
        this.beanDefinitionClass = beanDefinitionClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setScope(String scope) {
        this.scope = scope;
        singleton = ConfigurableBeanFactory.SCOPE_SINGLETON.equals(scope);
        prototype = ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }
}
