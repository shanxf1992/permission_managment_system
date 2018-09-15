package com.itheima.acl.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * 该工具的主要目的:
 * 将一个类, 转换成一个 json 对象.
 * 将 json 对象, 转换成系统中的 类对象
 */
@Slf4j
public class JsonMapper {

    //首先, 定义一个全局的 核心转换器
    private static ObjectMapper objectMapper = new ObjectMapper();

    //做一些变量的初始化
    static{
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    //将对象转换成一个字符串
    public static <T> String obj2String(T src) {
         if( src == null) return null;
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("parse Object to String exceptin!", e);
            return null;
        }
    }

    //将 String 转换成 Object
    public static <T> T string2Obj(String src, TypeReference<T> tTypeReference) {
        if(src == null || tTypeReference == null ) return null;
        try {
            return (tTypeReference.getType()).equals(String.class) ? (T) src : objectMapper.readValue(src, tTypeReference);
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{} --> TypeReference<T>:{} , error:{}", src, tTypeReference.getType(), e);
            return null;
        }
    }
}
