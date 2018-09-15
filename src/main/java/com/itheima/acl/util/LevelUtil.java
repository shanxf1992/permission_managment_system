package com.itheima.acl.util;

import org.apache.commons.lang3.StringUtils;

/**
 * LevelUtil 工具类主要用于 获取对应部门当前的当前级别 Level
 */
public class LevelUtil {

    // 设置连接符, 用于设置级别
    public final static String SEPARATOR = ".";
    // 设置最顶层级别为 ROOT, 值为 0
    public final static String ROOT = "0";

    /**
     *  getLevel(): 用于组装当前层级的方法
     *  @Param parentLevel: 当前部门的 父部门的级别
     *  @Param parentId: 当前部门的 父部门的 id
     *  组装方式:
     *      level = parentLevel.parentId
     */

    public static String getLevel(String parentLevel, Integer parentId) {
        //如果当前部门的父部门级别为空, 说明为ROOT
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }

}
