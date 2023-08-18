package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Menu;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.RoleMenu;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *

 */

/**
 * 权限菜单
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
        @Resource
        private RoleMenuMapper roleMenuMapper;
        @Resource
        private IMenuService menuService;
        @Transactional
        @Override
        public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
               /* QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("role_id", roleId);
                roleMenuMapper.delete(queryWrapper);*/
                // 先删除当前角色id所有的绑定关系
                roleMenuMapper.deleteByRoleId(roleId);
                for (Integer menuId : menuIds) {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        roleMenuMapper.insert(roleMenu);
                }
        }
        @Override
        public List<Integer> getRoleMenu(Integer roleId) {
                return roleMenuMapper.selectByRoleId(roleId);
        }
}
