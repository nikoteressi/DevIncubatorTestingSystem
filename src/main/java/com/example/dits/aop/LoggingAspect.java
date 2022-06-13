package com.example.dits.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Aspect
@Component
public class LoggingAspect {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Authentication auth;

    @Before("execution(* com.example.dits.controllers.TestPageController.*(..))")
    public void logAllMethods(JoinPoint joinPoint) {
        getAuth();
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + auth.getAuthorities().stream().findFirst().orElse(null) + " called method: " + joinPoint.getSignature().getName() + " with args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("execution(* com.example.dits.controllers.TestPageController.goTest(..))")
    public void logGoTest(JoinPoint joinPoint) throws Throwable {
        getAuth();
        HttpSession session = getSession(joinPoint.getArgs());
        String testName = (String) session.getAttribute("testName");
        String topicName = (String) session.getAttribute("topicName");
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " started test: " + topicName + "/ " + testName);
    }

    @After("execution(* com.example.dits.controllers.TestPageController.nextTestPage(..)))")
    public void logNextTestPage(JoinPoint joinPoint) throws Throwable {
        Object[] arguments = getArgumentsFromIndicatedMethod(joinPoint);
        List<Integer> answeredQuestions = getAnsweredQuestions(arguments);
        ModelMap modelMap = getModelMap(arguments);
        int questionNumber = (int) getSession(arguments).getAttribute("questionNumber") - 1;
        String question = modelMap.getAttribute("question") + "?";
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " answered on question: " + questionNumber + ". " + question + "?" + " with indexes of answers: " + answeredQuestions);
    }

    @After("execution(* com.example.dits.controllers.TestPageController.testStatistics(..))")
    public void logTestStatistic(JoinPoint joinPoint) {
        HttpSession session = getSession(getArgumentsFromIndicatedMethod(joinPoint));
        int quantityOfRightAnswers = (int) session.getAttribute("quantityOfRightAnswers");
        double rightAnswerPercent = (double) session.getAttribute("rightAnswerPercent");
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " finished test with following statistic: Right answers: " + quantityOfRightAnswers + "; " + (int)rightAnswerPercent + "% pass rate.");
    }

    private ModelMap getModelMap(Object[] arguments) {
        return (ModelMap) Arrays.stream(arguments).filter(f -> f.getClass().getName().toLowerCase(Locale.ROOT).contains("map")).findFirst().orElse(null);
    }

    private List<Integer> getAnsweredQuestions(Object[] arguments) {
        return (List<Integer>) Arrays.stream(arguments).filter(f -> f.getClass().getName().contains("List")).findFirst().orElse(null);
    }

    private Object[] getArgumentsFromIndicatedMethod(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    private HttpSession getSession(Object[] arguments) {
        return (HttpSession) Arrays.stream(arguments).filter(f -> f.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("session")).findFirst().orElse(null);
    }

    private void getAuth() {
        auth = SecurityContextHolder.getContext().getAuthentication();
    }
}
