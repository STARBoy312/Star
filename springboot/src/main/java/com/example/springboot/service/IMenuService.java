package com.example.springboot.service;

import com.example.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
public interface IMenuService extends IService<Menu> {
        //寻找菜单
        List<Menu> findMenus(String name);
}
