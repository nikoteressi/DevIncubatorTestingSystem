package com.example.dits.controllers;

import com.example.dits.DAO.*;
import com.example.dits.entity.Question;
import com.example.dits.entity.Topic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = "USER")
public class TestPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TopicRepository topicRepository;
    @MockBean
    TestRepository testRepository;
    @MockBean
    QuestionRepository questionRepository;
    @MockBean
    AnswerRepository answerRepository;
    @MockBean
    RoleRepository roleRepository;
    @MockBean
    UserRepository userRepository;

    private final MockHttpSession session = new MockHttpSession();

    @Test
    public void shouldReturnListWithAnswersAndQuestionInModelAndStatusOk() throws Exception {
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question("Question"));
        given(questionRepository.getQuestionsByTest_TestId(1)).willReturn(questionList);
        given(answerRepository.getAnswersByQuestion(questionList.get(0))).willReturn(new ArrayList<>());
        given(testRepository.getTestByTestId(1)).willReturn(new com.example.dits.entity.Test(1, "Descr", "Name", new Topic(), new ArrayList<>()));
        given(topicRepository.getTopicByName("Topic")).willReturn(new Topic(1, "Topic"));
        mockMvc.perform(get("/user/goTest?testId=1&theme=Topic"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("question"))
                .andExpect(model().attributeExists("answers"))
                .andExpect(model().attribute("title", "Testing"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void shouldReturnModelWithAttributesAndStatusOk() throws Exception {
//        mockMvc.perform(get("/user/resultPage"))
//                .andDo(print())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(model().attribute("title", "Result"))
//                .andExpect(model().attribute("rightAnswersPercent", anyInt()))
//                .andExpect(model().attribute("quantityOfRightAnswers", anyInt()))
//                .andExpect(status().isOk());
//    }
}
