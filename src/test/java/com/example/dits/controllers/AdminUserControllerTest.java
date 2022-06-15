package com.example.dits.controllers;

import com.example.dits.DAO.*;
import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
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
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = "ADMIN")
public class AdminUserControllerTest {
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

    @Test
    public void shouldReturnModelWithTitleSetAsUserEditorAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/users-list"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("title", "User editor"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListOfUsersAndStatusOk() throws Exception {
        mockMvc.perform(delete("/admin/remove-user?userId=1").with(csrf()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListWithUserInfoDTOAndStatusOkAfterAddNewUser() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO(0, "user", "user", "user", "ROLE_USER", "pass");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/admin/add-user").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoDTO)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestAfterAddNewUserIfNoData() throws Exception {
        mockMvc.perform(post("/admin/add-user").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnRedirectAfterUpdateUser() throws Exception {
        User user = new User();
        user.setUserId(2);
        user.setRole(new Role(1,"ROLE_USER", new ArrayList<>()));
        user.setFirstName("us");
        user.setLastName("er");
        user.setLogin("user");
        user.setPassword("jdhgsfysegyufhvkjsnlghj");
        given(userRepository.findById(2)).willReturn(Optional.of(user));
        given(roleRepository.getRoleByRoleName("ROLE_USER")).willReturn(new Role(1, "USER", new ArrayList<>()));
        UserInfoDTO userInfoDTO = new UserInfoDTO(2, "user", "user", "user", "ROLE_USER", "pass");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/admin/edit-user").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfoDTO)))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnBadRequestAfterUpdateNewUserIfNoData() throws Exception {
        mockMvc.perform(post("/admin/edit-user").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnListWithUsersAndStatusOk() throws Exception {
        mockMvc.perform(get("/admin/get-users"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUserInfoDtoAndStatusOkIfThereISId() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setRole(new Role(1,"ROLE_USER", new ArrayList<>()));
        user.setFirstName("us");
        user.setLastName("er");
        user.setLogin("user");
        user.setPassword("jdhgsfysegyufhvkjsnlghj");
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(get("/admin/get-users?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
