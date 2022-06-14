package com.example.dits.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLogger {

    private final Logger LOG;

    public ExceptionLogger() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    @AfterThrowing(value = "execution(* com.example.dits.controllers.*.*(..))", throwing = "ex")
    public void logAfterThrowingNotFoundException(JoinPoint joinPoint, Exception ex) {
        LOG.error("Thrown exception: "
                + "\"" + ex.getClass().getSimpleName() + "\""
                + " In method: " + "\""
                + joinPoint.getSignature().getName()
                + "\" ()" + "Message: " + "\""
                + ex.getMessage() + "\"");
    }
}
