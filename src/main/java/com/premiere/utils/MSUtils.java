package com.premiere.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MSUtils {


    /**
     * 加密算法
     *
     * @param password 密码
     * @return 加密后的密码
     * @throws NoSuchAlgorithmException ？
     */
    public static String EncodeByMD5(String password) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(password)) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        return base64Encoder.encode(md5.digest(bytes));
    }

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    public static String getCode() {
        //需要按照一定的规则opt验证码
        Random random = new Random();
        int anInt = random.nextInt(9999999);
        String code = anInt + "";
        code = code.substring(0, 6);
        return code;
    }


    public static String generaterOrderNo() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String replace = now.format(DateTimeFormatter.ISO_DATE_TIME)
                .replace("-", "")
                .replace(":", "")
                .replace("T", "")
                .replace(".", "");
        return stringBuilder.append(replace).append(getCode()).toString();
    }
    public static String dateToStr(DateTime date,String formatStr){
        if (date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }
}
