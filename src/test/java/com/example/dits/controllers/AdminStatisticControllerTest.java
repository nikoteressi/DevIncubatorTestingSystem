package com.example.dits.controllers;

import com.example.dits.service.StatisticService;
import com.example.dits.service.TestService;
import com.example.dits.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
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
    public void getAdminStatistic() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserStatistic() throws Exception {
        mockMvc.perform(get("/admin/user-statistic"))
                .andExpect(status().isOk());
    }

    @Test()
    public void getTestStatisticByTopicId() throws Exception {
        mockMvc.perform(get("/admin/getTestsStatistic")
                        .param("id", String.valueOf(anyInt())))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserStatisticsById() throws Exception {
        mockMvc.perform(get("/admin/get-users-statistic")
                        .param("userId", String.valueOf(anyInt())))
                .andExpect(status().isOk());
    }

    @Test
    public void removeStatisticById() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/byId")
                        .param("id", String.valueOf(anyInt())))
                .andExpect(status().isOk());
    }

    @Test
    public void removeAllStatistics() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/all"))
                .andExpect(redirectedUrl("/admin/adminStatistic"));
    }

}
