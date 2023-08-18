package com.example.springboot.service;

import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDto;
import com.example.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 *
 */
public interface IUserService extends IService<User> {
    //登录
    UserDto login(UserDto userDto);
    //注册
    User register(UserDto userDto);
    //更新密码
    void updatePassword(UserPasswordDto userPasswordDTO);
    //根据姓名查询
    User queryName(String userName);


}
