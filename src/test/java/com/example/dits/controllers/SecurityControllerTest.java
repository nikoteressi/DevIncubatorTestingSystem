package com.example.dits.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStatusOkWhenNoAuthorities() throws Exception {
        mockMvc.perform(get("/accessDenied"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnRedirectWhenLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}
