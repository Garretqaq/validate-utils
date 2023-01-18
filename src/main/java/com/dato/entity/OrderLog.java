package com.dato.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@TableName
@ToString
public class OrderLog {

    @TableId
    private Integer id;

    private String userName;

    private Long phone;

    private Date createTime;
}
