package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.RoleEnum;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDto;
import com.example.springboot.entity.Menu;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.service.IUserService;
import cn.hutool.log.Log;
import com.example.springboot.utils.TokenUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *

 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG=Log.get();
@Resource
private RoleMapper roleMapper;

@Resource
private UserMapper userMapper;
@Resource
private RoleMenuMapper roleMenuMapper;

@Resource
private IMenuService menuService;

    /**
     *
     * @param userDto
     * @return
     */
    @Override

public UserDto login(UserDto userDto) {
        // 用户密码 md5加密
    userDto.setPassword(SecureUtil.md5(userDto.getPassword()));
    User one=getUserInto(userDto);
    if(one !=null){
        BeanUtil.copyProperties(one,userDto,true);
            // 设置token
        String token=TokenUtils.genToken(one.getId().toString(), one.getPassword());
        userDto.setToken(token);
        String role=one.getRole();// ROLE_ADMIN
             // 设置用户的菜单列表
        List<Menu> roleMenus=getRoleMenus(role);
        userDto.setMenus(roleMenus);
        return userDto;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    /**
     *
     * @param userDto
     * @return将注册的用户信息储存到数据库中否则将报错
     */
    @Override
    public User register(UserDto userDto) {
        // 用户密码 md5加密
        userDto.setPassword(SecureUtil.md5(userDto.getPassword()));
        User one=getUserInto(userDto);
        if(one == null){
            one =new User();
            BeanUtil.copyProperties(userDto,one,true);
            // 默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_USER.toString());
            if (one.getNickname() == null) {
                one.setNickname(one.getUsername());
            }
            save(one);// 把 copy完之后的用户对象存储到数据库
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return null;
    }

    /**
     *
     * @param userPasswordDTO
     * 将密码进行修改
     */
    @Override
    public void updatePassword(UserPasswordDto userPasswordDTO) {
        userPasswordDTO.setPassword(SecureUtil.md5(userPasswordDTO.getPassword()));
        userPasswordDTO.setNewPassword(SecureUtil.md5(userPasswordDTO.getNewPassword()));
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    /**
     *根据姓名查询
     * @param userName
     * @return
     */
    @Override
    public User queryName(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userName);
        User one = getOne(queryWrapper);
        return one;
    }
    private User getUserInto(UserDto userDto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        User one;
        try{
            one = getOne(queryWrapper);

        }catch(Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    /**
     * 动态分配菜单
     * @param roleFlag
     * @return
     */
    @NotNull
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleId=roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds=roleMenuMapper.selectByRoleId(roleId);
        // 查出系统所有的菜单(树形)
        List<Menu> menus=menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus=new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }

            List<Menu> children=menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child ->!menuIds.contains(child.getId()));

        }
        return roleMenus;
    }
}
