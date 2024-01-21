package qh.springframework.context.realize.abstractrealize;

import qh.springframework.beans.factory.realize.DefaultListableBeanFactory;
import qh.springframework.beans.factory.realize.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] locations = getConfigLocations();
        if(null != locations){
            beanDefinitionReader.loadBeanDefinitions(locations);
        }

    }

    protected abstract String[] getConfigLocations();
}
