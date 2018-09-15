package com.itheima.acl.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextHelper : 用于获取 applicationContext 上下文
 *      通过注解, 将这个类交给 spring 来管理
 */

@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    // 定义一个全局的 Application Context
    private static ApplicationContext applicationContext;

    // 当系统启动的时候, 会将 ApplicationContext 注入到 下边方法的参数中
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 从 applicationContext 中 获取上下文的 bean
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) return null;
        return applicationContext.getBean(clazz);
    }

    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) return null;
        return applicationContext.getBean(name, clazz);
    }
}
