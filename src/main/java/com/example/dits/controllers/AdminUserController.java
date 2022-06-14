package com.example.dits.controllers;

import com.example.dits.dto.UserInfoDTO;
import com.example.dits.entity.Role;
import com.example.dits.entity.User;
import com.example.dits.service.RoleService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @GetMapping("/users-list")
    public String getUsers(ModelMap model) {
        model.addAttribute("title", "User editor");
        return "admin/user-editor";
    }

    @ResponseBody
    @DeleteMapping("/remove-user")
    public List<UserInfoDTO> removeUser(@RequestParam int userId) {
        userService.removeUser(userId);
        return getUsersList();
    }

    @ResponseBody
    @PostMapping("/add-user")
    public List<UserInfoDTO> adduser(@RequestBody UserInfoDTO userInfo) {
        User user = modelMapper.map(userInfo, User.class);
        Role role = roleService.getRoleByRoleName(userInfo.getRole());
        user.setRole(role);
        userService.save(user);
        return getUsersList();
    }

    @PostMapping("/edit-user")
    public String editUser(@RequestBody UserInfoDTO userInfo) {
        User user = modelMapper.map(userInfo, User.class);
        int userId = userInfo.getUserId();
        Role role = roleService.getRoleByRoleName(userInfo.getRole());
        user.setRole(role);
        userService.update(user, userId);
        return "redirect:/admin/get-users";
    }

    @ResponseBody
    @GetMapping("/get-users")
    public List<UserInfoDTO> getUsersList() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping("/get-user-by-id")
    public UserInfoDTO getUserById(@RequestParam int userId) {
        User user = userService.getUserById(userId);
        return convertToDTO(user);
    }

    private UserInfoDTO convertToDTO(User user) {
        return modelMapper.map(user, UserInfoDTO.class);
    }
}

