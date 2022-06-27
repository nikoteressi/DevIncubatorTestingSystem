package com.example.dits.controllers;

import com.example.dits.DAO.StatisticRepository;
import com.example.dits.DAO.TestRepository;
import com.example.dits.DAO.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
public class AdminStatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatusAccessDeniedWhenIsNotAdmin() throws Exception {
        mockMvc.perform(get("/admin/user-statistic")
                        .with(user("user").roles("USER")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnStatusOkAndListWithTopics() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic"))
                .andDo(print())
                .andExpect(model().attributeExists("topicList"))
                .andExpect(model().attribute("topicList", new ArrayList<>()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusOkAndListWithUsersWhenAdmin() throws Exception {
        mockMvc.perform(get("/admin/user-statistic"))
                .andDo(print())
                .andExpect(model().attributeExists("usersList"))
                .andExpect(model().attribute("usersList", new ArrayList<>()))
                .andExpect(status().isOk());
    }

    @Test()
    public void shouldReturnListWithTestStatisticsIfTopicExists() throws Exception {
        mockMvc.perform(get("/admin/getTestsStatistic?id=1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUsersAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/get-users-statistic")
                        .param("userId", String.valueOf(anyInt())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnSuccessAfterRemovingStatistic() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/byId")
                        .param("id", String.valueOf(anyInt())))
                .andDo(print())
                .andExpect(content().string("success"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnRedirectAfterRemoveAllStatistics() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/all"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

}
