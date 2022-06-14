package com.example.dits.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class AuthorisationLogger {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Authentication authentication;
    private String login;
    private String authority;


    @After("execution(* com.example.dits.controllers.SecurityController.loginHandle(..))")
    public void logLoginPage() {
        setUserInfo();
        LOG.info("User: " + "\"" + login + "\"" + " has been authorized with authorities: " + "\"" + authority + "\".");
    }

    @After("execution(* com.example.dits.controllers.SecurityController.logoutPage(..))")
    public void logLogoutPage() {
        LOG.info("User: " + "\"" + login + "\"" + " with authorities: " + "\"" + authority + "\"" + " logged out.");
    }

    private void setUserInfo() {
        getAuth();
        login = authentication.getName();
        authority = Objects.requireNonNull(authentication.getAuthorities().stream().findFirst().orElse(null)).getAuthority();
    }

    private void getAuth() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
    }

}
