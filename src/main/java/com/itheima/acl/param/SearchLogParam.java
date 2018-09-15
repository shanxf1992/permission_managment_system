package com.itheima.acl.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchLogParam {


    private Integer type; // 类型
    private String beforeSeg; // 操作之前的片段
    private String afterSeg; //  操作之后的片段
    private String operator; // 操作者
    private String fromTime; // 开始时间 yyyy-MM-dd HH:mm:ss
    private String toTime; // 结束时间

}
