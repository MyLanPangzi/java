package com.hiscat.spring.annotation;

import com.hiscat.spring.annotation.config.aop.AopConfig;
import com.hiscat.spring.annotation.config.aware.AwareConfig;
import com.hiscat.spring.annotation.config.profile.ProfileConfig;
import com.hiscat.spring.annotation.dao.BookDao;
import com.hiscat.spring.annotation.dao.BookDaoImpl2;
import com.hiscat.spring.annotation.entity.*;
import com.hiscat.spring.annotation.service.BookService;
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
     * 先是spring优先级比较高的BeanPostProcessor处理，再是标准注解BeanPostProcessor处理，接着再是实现了spring接口的方法
     * 初始化：BeanPostProcessor#before > javax标注注解 > 框架接口 > 自定义方法 > BeanPostProcessor#after
     * 销毁：javax标准注解 > 框架接口 > 自定义方法
     * invokeAwareMethods(beanName, bean);
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

    /**
     * Autowired Inject Resource
     * byType byName
     * Primary Qualifier
     */
    @Test
    void testAutowired() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                "com.hiscat.spring.annotation.config.autowire",
                "com.hiscat.spring.annotation.service",
                "com.hiscat.spring.annotation.dao"
        );
        BookDao bookDao = context.getBean(BookService.class).getBookDao();
        System.out.println(bookDao);
        assertEquals(bookDao.getClass(), BookDaoImpl2.class);
        context.close();
    }

    /**
     * Aware 获取Spring底层组件，在bean 生命周期之前，也是通过BeanPostProcessor处理
     */
    @Test
    void testAware() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwareConfig.class);
        System.out.println(context.getBean(AwareConfig.class));
        context.close();
    }

    /**
     * Profile 限定bean的激活范围，多环境切换，用在配置类，Bean方法
     * ActiveProfiles
     */
    @Test
    void testProfile() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProfileConfig.class);
        applicationContext.getEnvironment().setActiveProfiles("dev");
        applicationContext.refresh();
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        applicationContext.close();
    }

    /**
     * 支持五种切面，Around相当于拿到对象方法手动执行，优先级最高
     * Around Before After AfterReturning AfterThrowing
     */
    @Test
    void testAop() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AopConfig.class);
        assertEquals(0, applicationContext.getBean(AopConfig.Calculator.class).div(1, 10));
        assertThrows(Throwable.class, () -> applicationContext.getBean(AopConfig.Calculator.class).div(1, 0));
        applicationContext.close();

    }
}
