package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author 青哥哥
 * @since 2023-07-29
 */
@Getter
@Setter
  @TableName("sys_user")

@ToString
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * id
   */
  @ApiModelProperty("id")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /**
   * 用户名
   */
  @ApiModelProperty("用户名")
  private String username;
  /**
   * 密码
   */
  @ApiModelProperty("密码")
  private String password;
  /**
   * 昵称
   */
  @ApiModelProperty("昵称")
  private String nickname;
  /**
   * 邮箱
   */
  @ApiModelProperty("邮箱")
  private String email;
  /**
   * 电话
   */
  @ApiModelProperty("电话")
  private String phone;
  /**
   * 地址
   */
  @ApiModelProperty("地址")
  private String address;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private LocalDateTime createTime;
  @ApiModelProperty("头像")
  private String avtarUrl;
  @ApiModelProperty("角色")
  private String role;
}