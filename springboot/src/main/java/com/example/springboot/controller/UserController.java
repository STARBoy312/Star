package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDto;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IUserService;
import com.example.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>

 */
@RestController
@RequestMapping("/user")
public class UserController {

@Resource
private IUserService userService;


    /**
     * 登录接口
     * @param userDto 用户信息
     * @return 返回登录结果
     */
   @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        String username=userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto=userService.login(userDto);

        return Result.success(dto);
    }

    /**
     * 注册用户
     *
     * @param userDto 用户信息
     * @return 结果
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        String username=userDto.getUsername();
        String password = userDto.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
//通过用户名查询
        User user = userService.queryName(userDto.getUsername());
//查到的数据 和 前端传过来的数据 进行比较
        if(user != null) {
            if (user.getUsername().equals(userDto.getUsername())){
                return Result.error(Constants.CODE_401,"用户名重复，请重新输入");
            }
        }else {
            return Result.success(userService.register(userDto));
        }
        return null;
    }

    /**
     * 保存用户信息
     * @param user 用户信息
     * @return 返回保存结果
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        user.setPassword(SecureUtil.md5("1234@sQ"));
        user = userService.queryName(user.getUsername());
//查到的数据 和 前端传过来的数据 进行比较
        if (user != null) {
            if (user.getUsername().equals(user.getUsername())) {
                return Result.error(Constants.CODE_401, "用户名重复，请重新输入");
            }
        } else {
            return Result.success(userService.saveOrUpdate(user));
        }
        return null;
    }
    /**
     * 根据用户ID删除用户信息
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user != null && user.getRole().equals("ROLE_USER")) {
            boolean deleted = userService.removeById(id);
            if (deleted) {
                return Result.success(deleted);
            } else {
                return Result.error(Constants.CODE_401,"对不起，做不到");
            }
        } else {
            return Result.error(Constants.CODE_401,"对不起，做不到");
        }
    }

    /**
     *
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
        }

    /**
     * 查询所有用户
     *
     * @return 返回查询结果
     */
    @GetMapping
public Result findAll() {
        return Result.success(userService.list());
        }

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return 查询结果
     */
    @GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
        }

    /**
     * 更新用户密码
     *
     * @param userPasswordDto 更新密码所需的用户密码数据传输对象
     * @return 操作结果
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDto userPasswordDto) {

        userService.updatePassword(userPasswordDto);
        return Result.success();
    }

    /**
     * 根据用户名查找用户信息
     * @param username 用户名
     * @return 返回查询结果
     */
        @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @param email
     * @param address
     * @return
     */
    @GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
                queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
                queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
                queryWrapper.like("address", address);
        }
        return Result.success(userService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            users.add(user);
        }

        userService.saveBatch(users);
        return true;
    }
}

