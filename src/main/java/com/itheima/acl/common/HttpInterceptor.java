package com.itheima.acl.common;

import com.itheima.acl.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * HttpInterceptor : 用于 Http 前后请求的监听
 *      preHandle: 在请求处理之前进行调用
 *      postHandle: 只有在请求 正常 处理之后进行调用
 *      afterCompletion: 在任何情况下, 请求处理完成都会被调用
 *
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
//        log.info("request start : url-{}, params-{}", url, JsonMapper.obj2String(parameterMap));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
//        log.info("request finish : url-{}, params-{}", url, JsonMapper.obj2String(parameterMap));
        //super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
//        log.info("request exception : url-{}, params-{}", url, JsonMapper.obj2String(parameterMap));
        //super.afterCompletion(request, response, handler, ex);
        RemoveThreadLocalInfo();
    }

    private void RemoveThreadLocalInfo() {
        RequestHolder.remove();
    }
}
