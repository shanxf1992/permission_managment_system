package com.itheima.acl.filter;


import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.itheima.acl.common.ApplicationContextHelper;
import com.itheima.acl.common.JsonData;
import com.itheima.acl.common.RequestHolder;
import com.itheima.acl.domain.SysUser;
import com.itheima.acl.service.SysCoreService;
import com.itheima.acl.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 定义权限拦截
 *      1 首先定义一些白名单 ?
 */
@Slf4j
public class AclControllerFilter implements Filter {

    //声明一个全局的 url set
    private static Set<String> exclusionSet = Sets.newConcurrentHashSet();
    // 定义一个无权限访问后跳转的 url
    private final static String noAuthUrl = "/sys/User/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //读取配置文件中的 url 将其赋值到全局的 白名单列表中
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionSet = Sets.newConcurrentHashSet(exclusionUrlList);
        exclusionSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        // 1 取出请求中的 url
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map parameterMap = request.getParameterMap();

        System.out.println();
        System.out.println("servletPath : " + servletPath);
        System.out.println();

        //2 判断 请求的url, 是否存在于白名单中, 如果在, 直接通过
        if (exclusionSet.contains(servletPath)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        //3 进行权限拦截时, 保证其进过login 登陆过滤, 保证权限拦截的 url <= loginFilter 的url
        //3.1 为登陆的无权限呢访问
        SysUser sysUser = RequestHolder.getUserThreadLocal().get();
        if (sysUser == null) {
            // 用户为空值时, 进行无权限处理
            log.info("someone visit {}, but no login, paramter:{}", servletPath, JsonMapper.obj2String(parameterMap));
            noAuth(request, response);
            return;
        }

        //4 通过 SysCoreService 判断一个用户是否能够访问某一个 url
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUurlAcl(servletPath)) {
            log.info("{} visit {}, but nno login, parameter:{}", JsonMapper.obj2String(sysUser), servletPath, JsonMapper.obj2String(parameterMap));
            noAuth(request, response);
            return ;
        }

        //5 放行
        chain.doFilter(servletRequest, servletResponse);
    }

    // 处理用户的无权限访问
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(".json")) {
            JsonData jsonData = JsonData.fail("无权限访问, 请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().println(JsonMapper.obj2String(jsonData));
        } else {
            noAuthRedirect(noAuthUrl, response);
        }
    }

    // 跳转到指定页面
    private void noAuthRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        //跳转到指定url, 携带当前url
        response.getWriter().println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");

    }

    @Override
    public void destroy() {

    }
}
