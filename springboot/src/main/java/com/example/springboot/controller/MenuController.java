package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.Dict;
import com.example.springboot.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;

import com.example.springboot.service.IMenuService;
import com.example.springboot.entity.Menu;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    
@Resource
private IMenuService menuService;

@Resource
private DictMapper dictMapper;


    /**
     *新增或者更新
     * @param menu The menu item to be saved or updated
     * @return A success response indicating the successful save or update operation
     */
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveOrUpdate(menu);
        return Result.success();
    }
    /**
     * 利用id进行删除菜单
     *
     * @param id The ID of the menu item to be deleted
     * @return A success response indicating the successful deletion of the menu item
     */
    @DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
    menuService.removeById(id);
        return Result.success();
        }

    /**
     *
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
public Result deleteBatch(@RequestBody List<Integer> ids) {
    menuService.removeByIds(ids);
        return Result.success();
        }

    /**

     * 查询所有菜单
     * @param name 菜单名称（可选参数，默认为空）
     * @return 返回Result对象，包含查询到的菜单列表
     */
    @GetMapping
public Result findAll(  @RequestParam(defaultValue = "") String name) {
        return Result.success( menuService.findMenus(name));
        }

    /**
     * 根据ID查询单个菜单
     * @param id 菜单ID
     * @return 返回Result对象，包含查询到的单个菜单信息
     */
    @GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {

        return Result.success(menuService.getById(id));
        }

    /**

     * 分页查询菜单列表
     * @param pageNum 当前页码
     * @param name 菜单名称（模糊查询）
     * @param pageSize 每页显示的数据条数
     * @return 返回Result对象，包含查询到的分页结果
     */
    @GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
                       @RequestParam String name,
                       @RequestParam Integer pageSize) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

    /**
     * 获取图标列表
     * @return 返回Result对象，包含查询到的图标列表
     */
    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }
        }

