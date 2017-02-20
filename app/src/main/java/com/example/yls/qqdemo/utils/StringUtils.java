package com.example.yls.qqdemo.utils;

/**
 * Created by yls on 2016/12/29.
 */

public class StringUtils {

    private static final String REGEX_USER_NAME="^[a-zA-Z]\\w{2,19}$";
    private static final String REGEX_PASSWORD="^[0-9]{3,20}$";
    public static boolean isValidUserName(String userName){
            return userName.matches(REGEX_USER_NAME);
    }
    public static boolean isValidPassword(String password){
            return password.matches(REGEX_PASSWORD);
    }
}
