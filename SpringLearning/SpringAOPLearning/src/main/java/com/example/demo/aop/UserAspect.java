package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect // 告诉框架这是一个切面而不是普通的类
@Component // 随着项目的启动而启动
public class UserAspect {

    // 定义切点的注解，要配置拦截规则
    // execution 规定的 表示要执行括号内的规则
    // 第一个 * 表示不管拦截的方法的返回值 后面加上‘ ’， 然后加上包名加类名 .* 代表该类下的所有方法   后面的() 表示方法  ..表示不管方法的参数都拦截
    @Pointcut("execution(* com.example.demo.controller.UserController.*(..))")
    public void pointCut() {

    }

    // 定义前置通知
    @Before("pointCut()")
    public void beforeAdvice() {
        System.out.println("执行了前置通知");
    }

    // 定义后置通知
    @After("pointCut()")
    public void afterAdvice() {
        System.out.println("执行了后置通知");
    }

    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入环绕通知了");
        Object obj = null;
        obj = joinPoint.proceed();
        System.out.println("退出环绕通知");
        return obj;
    }
}
