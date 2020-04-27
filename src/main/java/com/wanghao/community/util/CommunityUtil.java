package com.wanghao.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
    //MD5加密
    public static String MD5(String password){
        if(StringUtils.isBlank(password)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
