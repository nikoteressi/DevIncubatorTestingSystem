package com.example.dits.service;

import com.example.dits.entity.Answer;
import com.example.dits.entity.Question;

import java.util.List;

public interface AnswerService {

    void create(Answer a);
    void update(Answer a, int id);
    void delete(Answer a);
    void save(Answer a);
    List<Answer> findAll();
    List<Answer> getAnswersFromQuestion(Question question);
    boolean isRightAnswer(List<Integer> answeredQuestion, Question question);

}
