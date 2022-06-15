package com.example.dits.controllers;

import com.example.dits.DAO.*;
import com.example.dits.dto.QuestionEditModel;
import com.example.dits.entity.Question;
import com.example.dits.entity.Role;
import com.example.dits.entity.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
public class AdminTestControllerTest {
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

    @Test
    public void shouldReturnModelWithTopicsListAndTitleSetAsTestEditorAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/testBuilder"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("topicLists"))
                .andExpect(model().attribute("title", "Test editor"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnTopicsListAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/getTopics"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnTestsListAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/getTests?id=1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNoTopicId() throws Exception {
        mockMvc.perform(get("/admin/getTests"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAAnswersListAndStatusOk() throws Exception {
        Question question = new Question();
        question.setAnswers(new ArrayList<>());
        given(questionRepository.getQuestionByQuestionId(1)).willReturn(question);
        mockMvc.perform(get("/admin/getAnswers?id=1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNoTestId() throws Exception {
        mockMvc.perform(get("/admin/getAnswers"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsWithAnswersDtoAndStatusOk() throws Exception {
        given(topicRepository.getTopicByTopicId(2)).willReturn(new Topic(2,"TopicOne", "DescrOne", new ArrayList<>()));
        mockMvc.perform(delete("/admin/removeTest?testId=1&topicId=2").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestWhenNoTestIdAndTopicID() throws Exception {
        mockMvc.perform(delete("/admin/removeTest").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

        @Test
    public void shouldReturnListWithTopicsDtoAndStatusOkAfterRemove() throws Exception {
        mockMvc.perform(delete("/admin/removeTopic?topicId=2").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestWhenThereISNoTopicId() throws Exception {
        mockMvc.perform(delete("/admin/removeTopic").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTopicsDtoAndStatusOkAfterCreateNew() throws Exception {
        mockMvc.perform(post("/admin/addTopic?name=Topic").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterCreateNewIfNoName() throws Exception {
        mockMvc.perform(post("/admin/addTopic").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsAndStatusOkAfterCreateNewTest() throws Exception {
        given(topicRepository.getTopicByTopicId(1)).willReturn(new Topic(1,"TopicOne", "DescrOne", new ArrayList<>()));
        mockMvc.perform(post("/admin/addTest?name=Test&description=Description&topicId=1").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterCreateNewTestIfNoData() throws Exception {
        mockMvc.perform(post("/admin/addTest").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTopicsAndStatusOkAfterUpdate() throws Exception {
        given(topicRepository.getTopicByTopicId(1)).willReturn(new Topic(1,"TopicOne", "DescrOne", new ArrayList<>()));
        mockMvc.perform(put("/admin/editTopic?id=1&name=Topees").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterUpdateTopicIfNoData() throws Exception {
        mockMvc.perform(put("/admin/editTopic").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsAndStatusOkAfterUpdate() throws Exception {
        given(testRepository.getTestByTestId(1)).willReturn(new com.example.dits.entity.Test());
        given(topicRepository.getTopicByTopicId(2)).willReturn(new Topic(2,"TopicOne", "DescrOne", new ArrayList<>()));
        mockMvc.perform(put("/admin/editTest?name=Topees&description=desc&testId=1&topicId=2").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterUpdateTestIfNoData() throws Exception {
        mockMvc.perform(put("/admin/editTest").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsWithQuestionsAndStatusOkAfterCreateNewQuestion() throws Exception {
        given(testRepository.getTestByTestId(1)).willReturn(new com.example.dits.entity.Test());
        given(topicRepository.getTopicByTopicId(1)).willReturn(new Topic(2,"TopicOne", "DescrOne", new ArrayList<>()));
        QuestionEditModel questionEditModel = new QuestionEditModel("question", 1, 2, 1, new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/admin/addQuestion").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionEditModel)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterCreateQuestionIfNoData() throws Exception {
        mockMvc.perform(post("/admin/addQuestion").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsWithQuestionsAndStatusOkAfterRemoveQuestion() throws Exception {
        given(topicRepository.getTopicByTopicId(1)).willReturn(new Topic(2,"TopicOne", "DescrOne", new ArrayList<>()));
        mockMvc.perform(delete("/admin/removeQuestion?questionId=1&topicId=1").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterRemoveQuestionIfNoData() throws Exception {
        mockMvc.perform(delete("/admin/removeQuestion").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithTestsWithQuestionsAndStatusOkAfterUpdateQuestion() throws Exception {
        given(topicRepository.getTopicByTopicId(1)).willReturn(new Topic(2,"TopicOne", "DescrOne", new ArrayList<>()));
        given(questionRepository.getQuestionByQuestionId(2)).willReturn(new Question());
        QuestionEditModel questionEditModel = new QuestionEditModel("question", 1, 2, 1, new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/admin/editQuestionAnswers").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionEditModel)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusBadRequestAfterUpdateQuestionIfNoData() throws Exception {
        mockMvc.perform(post("/admin/editQuestionAnswers").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListOfRoles() throws Exception {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "ADMIN", new ArrayList<>()));
        roles.add(new Role(1, "USER", new ArrayList<>()));
        List<String> namesRoles = new ArrayList<>();
        namesRoles.add("ADMIN");
        namesRoles.add("USER");
        given(roleRepository.findAll()).willReturn(roles);
        mockMvc.perform(get("/admin/getRoles"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(namesRoles.toString()))
                .andExpect(status().isOk());
    }


}
