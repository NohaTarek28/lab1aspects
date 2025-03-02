package com.example.demo.Aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    public MyAspect() {
        System.out.println("🔹 MyAspect is loaded into Spring context!");
    }

    @Before("execution(* com.example.demo.controllers.*.*(..))")
    public void logBeforeControllerMethods() {
        System.out.println("LOG: A controller method is about to be executed.");
    }
}
