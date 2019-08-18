package com.hiscat.spring.annotation.config.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Administrator
 */
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    @Pointcut("execution(public * com.hiscat.spring.annotation.config.aop.AopConfig.Calculator.*(..) )")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.printf("before name:%s,parameters:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        System.out.printf("after name:%s,parameters:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("around before name:%s,parameters:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        System.out.printf("around after name:%s,parameters:%s,result:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), result);
        return result;
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.printf("afterReturning name:%s,parameters:%s,result:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), result);
    }

    @AfterThrowing(value = "pointCut()", throwing = "t")
    public void afterThrowing(JoinPoint joinPoint, Throwable t) {
        System.out.printf("afterThrowing name:%s,parameters:%s,exception:%s\n", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), t);
    }

    @Component
    public static class Calculator {
        public double div(int i, int j) {
            return i / j;
        }
    }
}
