package com.wanghao.community.util;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 激活重复
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认用户会话过期时间（12小时）
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住用户会话过期时间（10天）
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 10;

}
