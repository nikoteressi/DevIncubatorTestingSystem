package com.example.dits.service.impl;

import com.example.dits.DAO.StatisticRepository;
import com.example.dits.dto.*;
import com.example.dits.entity.*;
import com.example.dits.service.StatisticService;
import com.example.dits.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;
    private final TopicService topicService;

    @Transactional
    @Override
    public void create(Statistic statistic) {
        repository.save(statistic);
    }

    @Transactional
    @Override
    public void update(Statistic statistic, int id) {
        Optional<Statistic> st = repository.findById(id);
        if (st.isPresent()) repository.save(statistic);
    }

    @Transactional
    @Override
    public void delete(Statistic statistic) {
        repository.delete(statistic);
    }

    @Transactional
    @Override
    public void removeStatisticByUserId(int userId) {
        repository.removeStatisticByUser_UserId(userId);
    }

    @Transactional
    @Override
    public void save(Statistic statistic) {
        repository.save(statistic);
    }

    @Override
    public void saveStatisticsToDB(List<Statistic> statistics) {
        for (Statistic statistic : statistics) {
            statistic.setDate(new Date());
            save(statistic);
        }
    }

    @Transactional
    @Override
    public void saveMapOfStat(Map<String, Statistic> map, String endTest) {
        for (Statistic st : map.values()) {
            st.setDate(new Date());
        }
    }

    @Transactional
    @Override
    public List<Statistic> findAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    @Override
    public List<Statistic> getStatisticByQuestion(Question question) {
        return repository.getStatisticByQuestion(question);
    }

    @Transactional
    @Override
    public List<TestStatisticByUser> getListOfTestsWithStatisticsByUser(User user) {
        List<Statistic> statisticsByUser = getUserStatistics(user);
        List<List<Statistic>> listStatisticsByTestName = getListStatisticsByTestName(statisticsByUser, getNamesOfTopicAndTest(statisticsByUser));
        return getTestStatisticsByUser(listStatisticsByTestName);
    }

    @Transactional
    @Override
    public List<TestStatisticByUser> getListOfTestsWithStatisticsByUserId(int id) {
        List<Statistic> statisticsByUser = getUserStatistics(id);
        List<List<Statistic>> listStatisticsByTestName = getListStatisticsByTestName(statisticsByUser, getNamesOfTopicAndTest(statisticsByUser));
        return getTestStatisticsByUser(listStatisticsByTestName);
    }

    @Transactional
    public List<TestStatistic> getListOfTestsWithStatisticsByTopic(int topicId) {
        Topic topic = topicService.getTopicByTopicId(topicId);
        return getTestStatistics(topic);
    }

    @Transactional
    @Override
    public List<Statistic> getUserStatistics(User user) {
        return repository.getStatisticsByUser(user);
    }

    @Transactional
    @Override
    public List<Statistic> getUserStatistics(int userId) {
        return repository.findAllByUser_UserId(userId);
    }

    private List<List<Statistic>> getListStatisticsByTestName(List<Statistic> statisticsByUser, List<String> namesOfTopicAndTest) {
        return fillListWithListsStatisticsByTestName(statisticsByUser, namesOfTopicAndTest);
    }

    private List<List<Statistic>> fillListWithListsStatisticsByTestName(List<Statistic> statisticsByUser, List<String> namesOfTopicAndTest) {
        List<List<Statistic>> listStatisticsByTestName = new ArrayList<>();
        for (String s : namesOfTopicAndTest) {
            listStatisticsByTestName.add(statisticsByUser.stream()
                    .filter(f -> s.contains(f.getQuestion().getTest().getName()))
                    .collect(Collectors.toList()));
        }
        return listStatisticsByTestName;
    }


    private List<TestStatisticByUser> getTestStatisticsByUser(List<List<Statistic>> listStatisticsByTestName) {
        List<TestStatisticByUser> testStatisticByUsers = new ArrayList<>();

        for (List<Statistic> s : listStatisticsByTestName) {
            testStatisticByUsers.add(new TestStatisticByUser(getFullNameOfTopicAndTest(s), s.size(), calculateAvg(s.size(), getAmountOfRightAnswer(s))));
        }
        return testStatisticByUsers;
    }

    private String getFullNameOfTopicAndTest(List<Statistic> s) {
        return s.stream().map(f -> f.getQuestion().getTest().getTopic().getName() + " / " + f.getQuestion().getTest().getName()).findFirst().orElse(null);
    }

    private List<String> getNamesOfTopicAndTest(List<Statistic> statisticsByUser) {
        List<Test> testsPassedByUser = getTestsPassedByUser(statisticsByUser);
        return testsPassedByUser
                .stream()
                .map(f -> f.getTopic().getName() + " / " + f.getName())
                .collect(Collectors.toList());
    }

    private List<Test> getTestsPassedByUser(List<Statistic> statisticsByUser) {
        return statisticsByUser
                .stream()
                .map(f -> f.getQuestion().getTest())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<TestStatistic> getTestStatistics(Topic topic) {
        List<Test> testLists = topic.getTestList();
        List<TestStatistic> testStatistics = new ArrayList<>();

        setTestLists(testLists, testStatistics);
        Collections.sort(testStatistics);
        return testStatistics;
    }

    private void setTestLists(List<Test> testLists, List<TestStatistic> testStatistics) {
        for (Test test : testLists) {

            List<Question> questionList = test.getQuestions();
            List<QuestionStatistic> questionStatistics = new ArrayList<>();
            QuestionStatisticAttempts statisticAttempts = new QuestionStatisticAttempts(0, 0, 0);
            setQuestionStatistics(questionList, questionStatistics, statisticAttempts);
            Collections.sort(questionStatistics);

            int testAverage = calculateTestAverage(statisticAttempts.getTestSumAvg(), questionStatistics.size());
            testStatistics.add(new TestStatistic(test.getName(), statisticAttempts.getNumberOfAttempts(),
                    testAverage, questionStatistics));
        }
    }

    private void setQuestionStatistics(List<Question> questionList, List<QuestionStatistic> questionStatistics,
                                       QuestionStatisticAttempts statisticAttempts) {
        for (Question question : questionList) {
            setQuestionStatisticAttempts(statisticAttempts, question);
            questionStatistics.add(new QuestionStatistic(question.getDescription(),
                    statisticAttempts.getNumberOfAttempts(), statisticAttempts.getQuestionAvg()));
        }
    }

    private void setQuestionStatisticAttempts(QuestionStatisticAttempts statisticAttempts, Question question) {
        List<Statistic> statisticList = getStatisticByQuestion(question);
        statisticAttempts.setNumberOfAttempts(statisticList.size());
        if (statisticAttempts.getNumberOfAttempts() > 0)
            statisticAttempts.setQuestionAvg(calculateAvg(statisticAttempts.getNumberOfAttempts(), getAmountOfRightAnswer(statisticList)));

        statisticAttempts.setTestSumAvg(statisticAttempts.getTestSumAvg() + statisticAttempts.getQuestionAvg());
    }

    private int getAmountOfRightAnswer(List<Statistic> statisticList) {
        return (int) statisticList.stream().filter(Statistic::isCorrect).count();
    }

    private int calculateTestAverage(int testSumAvg, int questionStatisticsSize) {
        if (questionStatisticsSize != 0)
            return testSumAvg / questionStatisticsSize;
        else
            return testSumAvg;
    }

    private int calculateAvg(int count, double rightAnswer) {
        return (int) (rightAnswer / count * 100);
    }
}
