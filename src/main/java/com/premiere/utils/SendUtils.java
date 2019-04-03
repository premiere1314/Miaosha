package com.premiere.utils;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SendUtils {

    /**
     *
     * @param telPhone telPhone
     * @param code code
     * @param host host
     * @param path path
     * @param appCode appCode
     * @param tplid tplid
     * @return 发送成功或失败
     */
    public static boolean sendCode(String telPhone,String code,String host,String path,String appCode,String tplid){
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", telPhone);
        querys.put("param", "code:"+code);
        querys.put("tpl_id", tplid);
        Map<String, String> bodys = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
