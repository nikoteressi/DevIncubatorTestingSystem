package com.example.dits.aspects;

import com.example.dits.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Profile("!Test")
public class AdministrationLogger {
    private final Logger LOG;
    private Authentication auth;
    private final ObjectMapper objectMapper;

    @Autowired
    public AdministrationLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    @After("execution(* com.example.dits.controllers.*.*(..))")
    public void logAllMethods(JoinPoint joinPoint) {
        getAuth();
        Object[] args = getArguments(joinPoint);
        ModelMap modelMap = (ModelMap) Arrays.stream(args).filter(f -> f.getClass().getSimpleName().contains("Map")).findFirst().orElse(null);
        if (modelMap != null) {
            String title = (String) modelMap.getAttribute("title");
            LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                    + auth.getAuthorities().stream().findFirst().orElse(null)
                    + "\"" + " visited page: " + "\"" + title + "\"");
        }
    }

    @After("execution(* com.example.dits.controllers.AdminUserController.adduser(..))")
    public void logAddUser(JoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = getArguments(joinPoint);
        UserInfoDTO user = (UserInfoDTO) Arrays.stream(args).filter(f -> f.getClass().getSimpleName().contains("UserInfo")).findFirst().orElse(null);

        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " created new user with following info: "
                + "\"" + objectMapper.writeValueAsString(user) + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminUserController.editUser(..))")
    public void logEditUser(JoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = getArguments(joinPoint);
        UserInfoDTO user = (UserInfoDTO) Arrays.stream(args).filter(f -> f.getClass().getSimpleName().contains("UserInfo")).findFirst().orElse(null);

        assert user != null;
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " edited user ith id: " + user.getUserId() + " with following info: "
                + "\"" + objectMapper.writeValueAsString(user) + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminUserController.removeUser(..))")
    public void logRemoveUser(JoinPoint joinPoint) {

        int userId = (int) getArguments(joinPoint)[0];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Removed user with id: " + "\"" + userId + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.addTopic(..))")
    public void logAddTopic(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        String topicName = (String) Arrays.stream(args).filter(f -> f.getClass().getSimpleName().contains("String")).findFirst().orElse(null);
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Added new topic: " + "\"" + topicName + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.editTopic(..))")
    public void logEditTopic(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        int id = (int) args[0];
        String topicName = (String) args[1];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Changed name of topic with id: " + id + " Changed name: " + "\"" + topicName + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.removeTopic(..))")
    public void logRemoveTopic(JoinPoint joinPoint) {
        int topicId = (int) getArguments(joinPoint)[0];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Removed topic with id: " + "\"" + topicId + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.addTest(..))")
    public void logAddTest(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        int topicId = (int) args[2];
        String testName = (String) args[0];
        String description = (String) args[1];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Added new test for topic with id: " + topicId + " Test Name: " + "\"" + testName + "\"" + " Test Description: " + "\"" + description + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.editTest(..))")
    public void logEditTest(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        int topicId = (int) args[3];
        int testId = (int) args[2];
        String testName = (String) args[0];
        String description = (String) args[1];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Edited test with id: " + "\"" + testId + "\"" + " for topic with id: " + topicId + " With following info:  Test Name: " + "\"" + testName + "\"" + " Test Description: " + "\"" + description + "\"");
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.removeTest(..))")
    public void logRemoveTest(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        int testId = (int) args[0];
        int topicId = (int) args[1];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Removed test with id: " + "\"" + testId + "\"" + " for topic with id: " + topicId);
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.addQuestion(..))")
    public void logAddQuestion(JoinPoint joinPoint) throws JsonProcessingException {
        QuestionEditModel questionEditModel = (QuestionEditModel) getArguments(joinPoint)[0];
        String question = questionEditModel.getQuestionName();
        int topicId = questionEditModel.getTopicId();
        int testId = questionEditModel.getTestId();
        List<AnswerEditModel> answers = questionEditModel.getAnswersData();
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Added new question to test with id: " + "\"" + testId + "\""
                + " for topic with id: " + topicId + " With following info:  Question: "
                + "\"" + question + "\"" + " Answers: " + objectMapper.writeValueAsString(answers));
    }

    private Object[] getArguments(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.editQuestionAnswers(..))")
    public void logEditQuestion(JoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = getArguments(joinPoint);
        QuestionEditModel questionEditModel = (QuestionEditModel) args[0];
        String question = questionEditModel.getQuestionName();
        int topicId = questionEditModel.getTopicId();
        int testId = questionEditModel.getTestId();
        List<AnswerEditModel> answers = questionEditModel.getAnswersData();
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Edited question to test with id: " + "\"" + testId + "\"" + " for topic with id: " + topicId + " With following info:  Question: "
                + "\"" + question + "\"" + " Answers: " + objectMapper.writeValueAsString(answers));
    }

    @After("execution(* com.example.dits.controllers.AdminTestController.removeQuestion(..))")
    public void logRemoveQuestion(JoinPoint joinPoint) {
        Object[] args = getArguments(joinPoint);
        int topicId = (int) args[1];
        int questionId = (int) args[0];
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " Removed question with id: " + "\"" + questionId + "\"" + " for topic with id: " + topicId);
    }

    @AfterReturning(value = "execution(* com.example.dits.controllers.AdminUserController.getUsersList(..))", returning = "usersList")
    public void logGetUsers(List<UserInfoDTO> usersList) throws JsonProcessingException {
        String jsonList = objectMapper.writeValueAsString(usersList);
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " gotten following users list: " + "\"" + jsonList + "\"");
    }

    @AfterReturning(value = "execution(* com.example.dits.controllers.AdminTestController.getTopicList(..))", returning = "topicsList")
    public void logGetTopics(List<TopicDTO> topicsList) throws JsonProcessingException {
        String jsonList = objectMapper.writeValueAsString(topicsList);
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " gotten following topics list: " + "\"" + jsonList + "\"");
    }

    @AfterReturning(value = "execution(* com.example.dits.controllers.AdminTestController.getTestsWithQuestions (..))", returning = "testList")
    public void logGetTests(List<TestWithQuestionsDTO> testList) throws JsonProcessingException {
        String jsonList = objectMapper.writeValueAsString(testList);
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " gotten following tests list: " + "\"" + jsonList + "\"");
    }

    @AfterReturning(value = "execution(* com.example.dits.controllers.AdminTestController.getTestsWithQuestions (..))", returning = "questionWithAnswers")
    public void logGetQuestionWithAnswers(QuestionWithAnswersDTO questionWithAnswers) throws JsonProcessingException {
        String jsonList = objectMapper.writeValueAsString(questionWithAnswers);
        LOG.info("User: " + "\"" + auth.getName() + "\"" + " with authorities: " + "\""
                + auth.getAuthorities().stream().findFirst().orElse(null)
                + "\"" + " gotten following question with answers: " + "\"" + jsonList + "\"");
    }

    private void getAuth() {
        auth = SecurityContextHolder.getContext().getAuthentication();
    }
}
