package com.itheima.acl.filter;

import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getContextPath();

        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/signin.jsp");
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(request);

        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
