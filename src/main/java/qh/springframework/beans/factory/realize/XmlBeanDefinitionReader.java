package qh.springframework.beans.factory.realize;

import cn.hutool.core.util.StrUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import qh.springframework.beans.core.io.Resource;
import qh.springframework.beans.core.io.ResourceLoader;
import qh.springframework.beans.factory.core.*;
import qh.springframework.beans.factory.realize.abstractrelize.AbstractBeanDefinitionReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try(InputStream is = resource.getInputStream()){
                doLoadBeanDefinitions(is);
            }
        }
        catch (IOException | ClassNotFoundException | BeansException | DocumentException e) {
            throw new BeansException("IOException parsing from XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);

    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }


    protected void doLoadBeanDefinitions(InputStream is) throws ClassNotFoundException, BeansException, DocumentException {

        SAXReader reader = new SAXReader();

        Document doc = reader.read(is);
        Element rootElement = doc.getRootElement();

        Element componentScan = rootElement.element("component-scan");
        if (null != componentScan) {
            String basePackage = componentScan.attributeValue("base-package");
            if (StrUtil.isEmpty(basePackage)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(basePackage);
        }

        List<Element> beanElements = rootElement.elements("bean");
        for (Element bean : beanElements) {

            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");
            String initMethodName = bean.attributeValue("init-method");
            String destroyMethodName = bean.attributeValue("destroy-method");
            String beanScope = bean.attributeValue("scope");

            Class<?> clazz = Class.forName(className);
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements("property");

            for (Element property : propertyList) {
                String attrName = property.attributeValue("name");
                String attrValue = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");

                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;

                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }

            if(getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate beanName[ " + beanName + " ] is not allowed");
            }

            getRegistry().registerBeanDefinition(beanName, beanDefinition);

        }

    }



    private void scanPackage(String basePackage) {
        String[] basePackages = StrUtil.splitToArray(basePackage, ',');
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(getRegistry());
        classPathBeanDefinitionScanner.doScan(basePackages);
    }


}

