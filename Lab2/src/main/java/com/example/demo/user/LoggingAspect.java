package com.example.demo.user;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    public LoggingAspect() {
        System.out.println("🔹 LoggingAspect is loaded into Spring context!");
    }

    @Before("execution(* com.example.demo.user.userController.*(..))")
    public void logBeforeControllerMethods(JoinPoint joinPoint) {
        System.out.println("LOG: Method " + joinPoint.getSignature().getName() + " is about to execute.");
    }
}
