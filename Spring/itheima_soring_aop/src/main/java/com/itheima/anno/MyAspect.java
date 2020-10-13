package com.itheima.anno;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component("myAspect")
@Aspect
public class MyAspect {

    @Pointcut("execution(* com.itheima.anno.*.*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(){
        System.out.println("前置增强....");
    }

    @After("MyAspect.pointcut()")
    public void afterReturning(){
        System.out.println("后置增强....");
    }
}
