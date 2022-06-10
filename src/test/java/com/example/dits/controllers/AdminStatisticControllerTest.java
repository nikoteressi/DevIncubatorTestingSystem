package com.example.dits.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminStatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "admin",
            authorities = {"ROLE_ADMIN"})

    @Test
    public void getAdminStatistic() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserStatistic() throws Exception {
        mockMvc.perform(get("/admin/user-statistic")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserStatisticsById() throws Exception {
        mockMvc.perform(get("/admin/get-users-statistic")
                        .param("userId", String.valueOf(anyInt()))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void removeStatisticById() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/byId")
                        .param("id", String.valueOf(anyInt()))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    public void removeAllStatistics() throws Exception {
        mockMvc.perform(get("/admin/adminStatistic/removeStatistic/all")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(redirectedUrl("/admin/adminStatistic"));
    }

}
