package qh.springframework;


import org.junit.Test;
import qh.springframework.bean.*;
import qh.springframework.context.realize.ClassPathXmlApplicationContext;

public class ApiTest {

    @Test
    public void test_convert() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        System.out.println("测试结果：" + husband);
    }



}
