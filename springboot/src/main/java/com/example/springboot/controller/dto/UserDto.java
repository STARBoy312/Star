package com.example.springboot.controller.dto;


import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //头像
    private String avtarUrl;

    private String token;
    //权限
    private String role;
    //菜单
    private List<Menu> menus;
}
