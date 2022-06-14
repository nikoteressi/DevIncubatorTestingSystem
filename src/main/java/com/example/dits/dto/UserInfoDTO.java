package com.example.dits.dto;

import com.example.dits.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private int userId;
    private String firstName;
    private String lastName;
    private String login;
    private String role;
    private String password;

}
