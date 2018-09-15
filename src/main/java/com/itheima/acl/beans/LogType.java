package com.itheima.acl.beans;

/**
 * 用来记录更新的类型
 */
public interface LogType {

    // 部门
    int TYPE_DEPT = 1;
    // 用户
    int TYPE_USER = 2;
    // 权限模块
    int TYPE_ACL_MODULE = 3;
    // 权限点
    int TYPE_ACL = 4;
    // 角色
    int TYPE_ROLE = 5;
    // 角色 和 权限
    int TYPE_ROLE_ACL = 6;
    // 角色 和 用户
    int TYPE_ROLE_USER = 7;


}
