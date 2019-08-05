package com.hyl.atcrowdfunding.utils;

/**
 * @author: hyl
 * @date: 2019/07/13
 **/
public class StringUtil {

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s); 
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
}
