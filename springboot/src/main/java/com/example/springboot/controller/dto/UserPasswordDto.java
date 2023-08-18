package com.example.springboot.controller.dto;

import lombok.Data;

@Data
public class UserPasswordDto {
    //用户名
    private String username;
    //密码
    private String password;
    //新密码
    private String newPassword;
}
