package com.orioninc.blogEducationProject.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LogAspect {
    private final Logger LOG = LogManager.getLogger(this.getClass());

    @Pointcut("execution(public * com.orioninc.blogEducationProject.controller.*.*(..))")
    public void loggingControllerParams(){}

    @Pointcut("execution(public * com.orioninc.blogEducationProject.service.*.*(..))")
    public void loggingServiceArgsAndReturnValue(){}

    @Before("loggingControllerParams()")
    public void logController(JoinPoint jp){
        StringBuilder msg = new StringBuilder("Class: ");
        Signature signature = jp.getSignature();
        msg.append(signature.getDeclaringType().getName()).append(", ");
        msg.append("Method: ").append(signature.getName()).append(", ");
        msg.append("Args: {");
        Arrays.stream(jp.getArgs()).forEach(arg -> msg.append(arg).append(", "));
        msg.append("}");
        LOG.info(msg);
    }



 @Around("loggingServiceArgsAndReturnValue()")
    public Object logParamsAndReturnValue(ProceedingJoinPoint jp) throws Throwable {
        StringBuilder msg = new StringBuilder("Class: ");
        Signature signature = jp.getSignature();
        msg.append(signature.getDeclaringTypeName()).append(", ");
        msg.append("Method: ").append(signature.getName()).append(", ");
        msg.append("Args: ");
        Arrays.stream(jp.getArgs()).forEach(arg -> msg.append(arg).append(", "));
        Object returnValue = jp.proceed();
        msg.append("Return: ").append(returnValue);

        LOG.debug(msg.toString());
        return returnValue;
    }

}
