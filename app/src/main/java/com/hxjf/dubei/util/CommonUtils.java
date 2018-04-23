package com.hxjf.dubei.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by Chen_Zhang on 2017/7/20.
 */

public class CommonUtils {
    // 判断手机号码是否有效
    public static boolean isValidTelNumber(String telNumber) {
        if (!TextUtils.isEmpty(telNumber)) {
            String regex = "(\\+\\d+)?1[34578]\\d{9}$";
            return Pattern.matches(regex, telNumber);
        }
        return false;
    }

}
