package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;

import com.example.springboot.service.IRoleService;
import com.example.springboot.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *

 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;
    /**
     * 保存角色信息
     *
     * @param role 角色对象
     * @return 返回保存结果
     */
    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }
    /**
     * 根据ID删除角色信息
     *
     * @param id 角色ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        if (role == null) {
            return Result.error(Constants.CODE_401,"对不起，做不到");
        }

        String roleName = role.getName();
        if ("管理员".equals(roleName) || "普通用户".equals(roleName)) {
            return Result.error(Constants.CODE_401,"对不起，做不到");
        }

        roleService.removeById(id);
        return Result.success();
    }


    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.success();
    }
    /**
     * 查询所有角色信息
     * @return 返回结果对象包含所有角色信息
     */
    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }
    /**
     * 根据ID查找角色
     * @param id 角色ID
     * @return 返回查找到的角色
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }
    /**
     * 分页查询角色信息
     *
     * @param name 角色名称
     * @param pageNum 当前页码
     * @param pageSize 每页显示的记录数
     * @return 查询结果
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    /**
     * 设置角色菜单权限
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 操作结果
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    /**
     * 根据角色ID获取与该角色相关的菜单项。
     *
     * @param roleId 角色ID，用于获取相关菜单项
     * @return 成功响应，包含与指定角色相关的菜单项
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success( roleService.getRoleMenu(roleId));
    }


}

