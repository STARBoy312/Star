package com.example.springboot.service;

import com.example.springboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface IRoleService extends IService<Role> {
    //设置权限菜单
    void setRoleMenu(Integer roleId, List<Integer> menuIds);
    //获得权限菜单
    List<Integer> getRoleMenu(Integer roleId);

}
