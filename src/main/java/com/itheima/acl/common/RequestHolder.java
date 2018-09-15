package com.itheima.acl.common;

import com.itheima.acl.domain.SysUser;
import javax.servlet.http.HttpServletRequest;

/**
 * RequestHolder : 用来存放当前用户的信息
 *      userThreadLocal : 用户的登陆信息
 *      requestThreadLocal : 用户的请求访问信息
 */

public class RequestHolder {

    private static ThreadLocal<SysUser> userThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    // 添加用户登陆信息
    public static void add(SysUser sysUser) {
        userThreadLocal.set(sysUser);
    }

    //添加用户请求信息
    public static void add(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    //获取用户信息
    public static ThreadLocal<SysUser> getUserThreadLocal() {
        return userThreadLocal;
    }

    //获取用户请求信息
    public static ThreadLocal<HttpServletRequest> getRequestThreadLocal() {
        return requestThreadLocal;
    }

    //移除当前用户信息
    public static void remove() {
        userThreadLocal.remove();
        requestThreadLocal.remove();
    }

}
