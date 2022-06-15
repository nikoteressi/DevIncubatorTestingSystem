package com.example.dits.controllers;

import com.example.dits.entity.*;
import com.example.dits.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class TestPageController {

    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final StatisticService statisticService;
    private final TopicService topicService;

    @GetMapping("/chooseTest")
    public String userPage(ModelMap model) {

        List<Topic> topicList = topicService.findAll();
        List<Topic> topicsWithQuestions = new ArrayList<>();
        for (Topic i : topicList) {
            if (i.getTestList().size() != 0) {
                topicsWithQuestions.add(i);
            }
        }
        model.addAttribute("title", "Testing");
        model.addAttribute("topicWithQuestions", topicsWithQuestions);
        return "user/chooseTest";
    }

    @GetMapping("/goTest")
    public String goTest(@RequestParam int testId, @RequestParam(value = "theme") String topicName, ModelMap model, HttpSession session) {

        List<Question> questionList = questionService.getQuestionsByTest_TestId(testId);
        int quantityOfQuestions = questionList.size();
        int questionNumber = 0;
        List<Answer> answers = answerService.getAnswersFromQuestion(questionList.get(questionNumber));
        int quantityOfRightAnswers = 0;

        int topicId = topicService.getTopicByName(topicName).getTopicId();
        String testName = testService.getTestByTestId(testId).getName();
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        session.setAttribute("testName", testName);
        session.setAttribute("topicName", topicName);
        session.setAttribute("topicId", topicId);
        session.setAttribute("questionSize", quantityOfQuestions);
        session.setAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        session.setAttribute("statistics", new ArrayList<Statistic>());
        session.setAttribute("questions", questionList);
        session.setAttribute("questionNumber", ++questionNumber);

        model.addAttribute("question", questionDescription);
        model.addAttribute("answers", answers);
        model.addAttribute("title", "Testing");
        return "user/testPage";
    }

    @GetMapping("/nextTestPage")
    public String nextTestPage(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                               ModelMap model,
                               HttpSession session) {

        List<Question> questionList = (List<Question>) session.getAttribute("questions");
        int questionNumber = (int) session.getAttribute("questionNumber");
        int quantityOfRightAnswers = (int) session.getAttribute("quantityOfRightAnswers");
        User user = (User) session.getAttribute("user");
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion, questionList.get(questionNumber - 1));

        List<Answer> answers = answerService.getAnswersFromQuestion(questionList.get(questionNumber));
        String questionDescription = questionService.getDescriptionFromQuestionList(questionList, questionNumber);

        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        statisticList.add(Statistic.builder()
                .question(questionList.get(questionNumber - 1))
                .user(user)
                .correct(isCorrect).build());
        if (isCorrect) ++quantityOfRightAnswers;
        session.setAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        session.setAttribute("statistics", statisticList);
        session.setAttribute("questionNumber", ++questionNumber);
        model.addAttribute("question", questionDescription);
        model.addAttribute("answers", answers);
        model.addAttribute("title", "Testing");
        return "user/testPage";
    }

    @GetMapping("/test-result")
    public String testStatistic(@RequestParam(value = "answeredQuestion", required = false) List<Integer> answeredQuestion,
                                HttpSession session) {
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        int questionNumber = questions.size();

        int quantityOfRightAnswers = (int) session.getAttribute("quantityOfRightAnswers");
        int questionQuantity = (int) session.getAttribute("questionSize");
        boolean isCorrect = answerService.isRightAnswer(answeredQuestion, questions.get(questionNumber - 1));
        User user = (User) session.getAttribute("user");
        List<Statistic> statisticList = (List<Statistic>) session.getAttribute("statistics");
        checkIfResultPage(questions, questionNumber, isCorrect, user, statisticList);
        statisticService.saveStatisticsToDB(statisticList);

        if (isCorrect) ++quantityOfRightAnswers;
        double rightAnswerPercent = (double) quantityOfRightAnswers / questionQuantity * 100;

        session.setAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        session.setAttribute("rightAnswerPercent", rightAnswerPercent);
        return "redirect:user/resultPage";
    }

    @GetMapping("/resultPage")
    public String testStatistics(ModelMap model, HttpSession session) {
        int quantityOfRightAnswers = (int) session.getAttribute("quantityOfRightAnswers");
        int rightAnswerPercent = (int) Math.round((Double) session.getAttribute("rightAnswerPercent"));
        model.addAttribute("title", "Result");
        model.addAttribute("rightAnswersPercent", rightAnswerPercent);
        model.addAttribute("quantityOfRightAnswers", quantityOfRightAnswers);
        return "user/result-page";
    }

    private void checkIfResultPage(List<Question> questions, int questionNumber, boolean isCorrect, User user, List<Statistic> statisticList) {
        if (!isResultPage(questionNumber, statisticList.size())) {
            statisticList.add(Statistic.builder()
                    .question(questions.get(questionNumber - 1))
                    .user(user)
                    .correct(isCorrect).build());
        }
    }

    private boolean isResultPage(int questionNumber, int statisticSize) {
        return statisticSize >= questionNumber;
    }


}
