package com.itheima.acl.common;

import com.itheima.acl.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import com.itheima.acl.exception.PermissionException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ExceptionResolver : 全局异常处理类 需要实现 HandlerExceptionResolver 接口
 *
 * 如果 ExceptionResolver  被 spring 管理的话, 那么 全局异常在进行 http 返回的时候就被被这个类捕捉到
 *
 * 最后需要将该类 交给 spring 管理, 这样全局抛出异常时, 才能通过它来接受
 */

@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {

        //1 定义返回值 ModelAndView
        ModelAndView modelAndView = null;

        //2 根据 request 获得返回的异常的 url
        String url = request.getRequestURI().toString();

        //3 定义一个默认异常信息
        String defaultMsg = "System error!";

        /**
         * 项目规范:
         *      项目中所有请求 Json 数据的请求, 都已 .json 结尾
         *      项目中所有请求 页面 的请求, 都已 .page 结尾
         *
         *      这样可以很容易的判断出, 当请求出现异常时, 该异常是 json 请求, 还是 页面 请求, 可以根据对应异常返回异常信息
         */
        // 如果该异常请求的 url 是以 json 请求
        if(url.endsWith(".json")){

            // 如果抛出的异常, 为自己定义的异常, 就不返回默认的异常信息
            if (exception instanceof PermissionException || exception instanceof ParamException) {

                System.out.println(exception.getMessage());
                JsonData jsonData = JsonData.fail(exception.getMessage());
                // jsonView 表示 配置文件中的 json 处理类( MappingJackson2JsonView: 处理的结果以 json 返回的时候, 会使用 该类来处理)
                modelAndView = new ModelAndView("jsonView", jsonData.toMap());
            } else {
                log.error("unknown json exception, url: " + url, exception);
                // 如果捕获的异常, 不是自己定义的异常, 就返回 默认的异常信息
                JsonData jsonData = JsonData.fail(defaultMsg);
                modelAndView = new ModelAndView("jsonView", jsonData.toMap());
            }

        // 如果该异常请求的 url 是以 json 请求
        } else if (url.endsWith(".page")) {
            log.error("unknow page exception, url: " + url, exception);
            JsonData jsonData = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("exception", jsonData.toMap());
        } else {
            log.error("unknow exception, url: " + url, exception);
            JsonData jsonData = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("jsonView", jsonData.toMap());
        }

        return modelAndView;
    }
}
