package com.itheima.acl.controller;

import com.itheima.acl.domain.SysUser;
import com.itheima.acl.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UserController : 用于处理用户自身的一些操作
 *      login() : 用户登陆
 */

@Controller
public class UserController {

    @Value("#{sysUserService}")
    private SysUserService sysUserService;

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 根据用户名, 密码查询用户
        SysUser sysUser = sysUserService.getUserByUserNameAndPassword(username, password);

        System.out.println(sysUser);
        String errorMsg = "";
        String ret = request.getParameter("ret"); // 用来存储原来的所在页面

        // 判断是否合法
        if (sysUser == null) {
            errorMsg = "用户名或者密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "改用户以被冻结";
        } else {
            // 执行登陆操作
            //1 将用户的登陆信息, 存放到 session 中
            request.getSession().setAttribute("user", sysUser);
            //登陆完成后, 跳转到原来的页面
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.page"); //TODO:
                return;
            }
        }

        // 返回错误的登陆信息
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }

        request.getRequestDispatcher("/signin.jsp").forward(request, response);
        return ;
    }

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("signin.jsp");
    }

}
