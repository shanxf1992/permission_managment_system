package com.itheima.acl.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Getter 自动添加 getXxx() 方法
 * @Setter 自动添加 setXxx() 方法
 * JsonData 用与处理当请求Json 数据的返回信息
 *      ret: true 表示当前请求是被正常处理, false 表示处理出现异常
 *      msg: 异常信息
 *      data: 返回的数据
 *
 *      success: 成功返回时调用的方法
 *      fail: 失败时调用的方法
 */

@Getter
@Setter
public class JsonData {

    private boolean ret;
    private String msg;
    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }


    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        return jsonData;
    }

    public static JsonData success(Object data, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = data;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public  Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("ret", ret);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }
}
