package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
    
/**
 * <p>
 * 
 * </p>
 *
 */
@Getter
@Setter
@TableName("sys_menu")
public class Menu implements Serializable {

private static final long serialVersionUID = 1L;

/*** id*/
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

/*** 名称*/
private String name;

/*** 路径*/
private String path;

/*** 图标*/
private String icon;

/*** 描述*/
private String description;
@TableField(exist=false)
private List<Menu> children;
private Integer pid;
}
