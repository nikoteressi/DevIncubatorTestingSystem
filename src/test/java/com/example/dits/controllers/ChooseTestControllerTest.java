package com.example.dits.controllers;

import com.example.dits.DAO.TestRepository;
import com.example.dits.DAO.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = "USER")
public class ChooseTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TestRepository testRepository;

    @Test
    public void shouldReturnListWithTestInfoDtoAndStatusOk() throws Exception {
        given(testRepository.getTestsByTopicName("topic")).willReturn(new ArrayList<>());
        mockMvc.perform(get("/user/chooseTheme?topicName=topic"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusOkIfNoTopicName() throws Exception {
        mockMvc.perform(get("/user/chooseTheme"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
