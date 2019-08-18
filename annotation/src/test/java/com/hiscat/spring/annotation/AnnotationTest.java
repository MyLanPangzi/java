package com.hiscat.spring.annotation;

import com.hiscat.spring.annotation.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationTest {

    /**
     * Component Repository Service Controller Configuration Bean
     * Import ImportSelector ImportBeanDefinitionRegistrar
     * FactoryBean
     */
    @Test
    void testImport() {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.hiscat.spring.annotation.config.imp");
        assertEquals(1, context.getBeansOfType(MyColor.class).size());
        assertEquals(1, context.getBeansOfType(Blue.class).size());
        assertEquals(1, context.getBeansOfType(Red.class).size());
        assertEquals(1, context.getBeansOfType(Person.class).size());
        assertSame(context.getBean(Person.class), context.getBean(Person.class));
        assertNotNull(context.getBean("&person"));
        System.out.println(context.getBean("&person"));
        Arrays.stream(context.getBeanDefinitionNames())
                .forEach(System.out::println);
    }

    /**
     * 初始化：BeanPostProcessor#before > javax标注注解 > 框架接口 > 自定义方法 > BeanPostProcessor#after
     * 销毁：javax标准注解 > 框架接口 > 自定义方法
     * populateBean(beanName, mbd, instanceWrapper);
     * exposedObject = initializeBean(beanName, exposedObject, mbd);
     * ****invokeAwareMethods(beanName, bean);
     * ****wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
     * ****invokeInitMethods(beanName, wrappedBean, mbd);
     * ****wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
     * registerDisposableBeanIfNecessary(beanName, bean, mbd);
     */
    @Test
    void testLieCycle() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.hiscat.spring.annotation.config.lifecycle");
        assertNotNull(context.getBean(Car.class));
        context.close();
    }

    /**
     * 字符串 原始值
     * SpEl #{}
     * 环境变量值 ${}
     */
    @Test
    void testPropertyValue() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.hiscat.spring.annotation.config.value");
        assertEquals("小花", context.getBean(Cat.class).getName());
        assertEquals(2, context.getBean(Cat.class).getAge());
        assertEquals("张三", context.getBean(Person.class).getName());
        assertEquals("M", context.getBean(Person.class).getGender());
        context.close();
    }
}
