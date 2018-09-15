package com.itheima.acl.util;

import java.util.Random;

/**
 * PasswordUtils : password 工具类, 用于随机生成 password
 */
public class PasswordUtils {

    private PasswordUtils() { }

    private final static String[] word = {
        "a", "b", "c", "d", "e", "f", "g",
        "h", "j", "k", "m", "n", "2", "3",
        "4", "5", "6", "7", "8", "9", "10"
    };

    // 随机生成密码
    public static String randomPassword() {
        StringBuffer password = new StringBuffer();
        int length = new Random().nextInt(3) + 6;
        for (int i = 0; i < length; i++) {
            password.append(word[new Random().nextInt(word.length)]);
        }
        return password.toString();
    }
}
