package com.itheima.acl.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class SearchLogDto {

    private Integer type; // 类型
    private String beforeSeg; // 操作之前的片段
    private String afterSeg; //  操作之后的片段
    private String operator; // 操作者
    private Date fromTime; // 开始时间 yyyy-MM-dd HH:mm:ss
    private Date toTime; // 结束时间

}
