package com.renx.commom.utils;

/**
 * @author liuqiongyu
 * @date 2018/12/05
 */
public interface CommonConstant {
    /**
     * token请求头名称
     */
    final static String ADMIN_REQ_TOKEN = "admin_token";
    final static String USER_REQ_TOKEN = "token";

    /**
     * token在REDIS中的前置
     */
    final static  String TOKEN_REDIS_KEY_PREFIX="mhos:admin_token:";
    final static  String USER_TOKEN_REDIS_KEY_PREFIX="mhos:user_token:";
    /**
     * 缓存请求所带参数的redis key
     */
    final static  String REQ_PARAMS_REDIS_KEY_PREFIX="mhos:req_params:";

    /**
     * 第三方的token，key为前缀+token，value为json（包含appId）
     */
    final static  String THIRDAPI_TOKEN_REDIS_KEY_PREFIX="mhos:thirdapi:token:";
    /**
     * 第三方的token（反向），key为前缀+appId，value为token
     */
    final static  String THIRDAPI_TOKEN_REF_REDIS_KEY_PREFIX="mhos:thirdapi:token_ref:";

    /**
     * 验证码CAPTCHA在REDIS中的前置
     */
    final static  String CAPTCHA_REDIS_KEY_PREFIX="mhos:captcha:";

    /**
     * 固定的内部调用接口使用的appId
     */
    final static String INNER_APPID = "inner_001";

    /**
     * 内部接口token调用在REDIS中的key
     */
    final static String INNNER_TOKEN_KEY = "mhos:inner:token";

    /**
     * 支付宝刷脸支付缓存ftoken->uid关系在REDIS中的前置
     */
    final static String FTOKEN_UID_REDIS_KEY_PREFIX="mhos:ftoken_uid:";

    /**
     * admin token超时时间
     */
    final static int TOKEN_REDIS_TIME_OUT = 60*60*3; //3小时

    /**
     * 验证码超时时间
     */
    final static int CAPTCHA_REDIS_TIME_OUT = 60*5; //5分钟

    final static int THIRDAPI_TOKEN_REDIS_TIME_OUT = 60*60*3; //3小时

    final static int REQ_PARAMS_REDIS_TIMEOUT_OUT = 60*5;// 5分钟

    /**
     * 支付宝刷脸支付缓存ftoken->uid超时时间
     */
    final static int FTOKEN_UID_REDIS_TIME_OUT = 60*10; //10分钟

}
