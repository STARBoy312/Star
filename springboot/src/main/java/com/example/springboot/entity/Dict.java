package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("sys_dict")
@Data
public class Dict {
    private String name;
    private String value;
    private String type;
}
