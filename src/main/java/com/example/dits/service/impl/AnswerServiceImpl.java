package com.example.dits.service.impl;

import com.example.dits.DAO.AnswerRepository;
import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;
import com.example.dits.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository repo;

    @Transactional
    public void create(Answer a) {
        repo.save(a);
    }

    @Transactional
    public void update(Answer a, int id) {
        Optional<Answer> answer = repo.findById(id);
        if (answer.isPresent())
            repo.save(a);
    }

    @Transactional
    public void delete(Answer a) {
        repo.delete(a);
    }

    @Transactional
    public void save(Answer a) {
        repo.save(a);
    }

    @Transactional
    public List<Answer> findAll() {
        return repo.findAll();
    }

    @Transactional
    public List<Answer> getAnswersByQuestion(Question question) {
        return repo.getAnswersByQuestion(question);
    }

    @Override
    public List<Answer> getAnswersFromQuestion (Question question) {
        return getAnswersByQuestion(question);
    }

    @Override
    public boolean isRightAnswer(List<Integer> userQuestionAnswers, Question question) {
        List<Answer> questionAnswers = getQuestionAnswers(question);
        List<Integer> rightIndexesList = getListOfIndexesOfRightAnswers(questionAnswers);
        if (rightIndexesList.isEmpty() && userQuestionAnswers == null) {
            return true;
        }
        if (!rightIndexesList.isEmpty() && userQuestionAnswers == null) {
            return false;
        }
        return userQuestionAnswers.equals(rightIndexesList);
    }

    private List<Integer> getListOfIndexesOfRightAnswers(List<Answer> questionAnswers) {
        List<Integer> rightAnswers = new ArrayList<>();
        for (int i = 0; i < questionAnswers.size(); i++) {
            if (questionAnswers.get(i).isCorrect()) {
                rightAnswers.add(i);
            }
        }
        return rightAnswers;
    }

    private List<Answer> getQuestionAnswers(Question question) {
        return getAnswersByQuestion(question);
    }

}
