package com.renx.wechatserver.weixin;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WXPublicUtils {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(WXPublicUtils.class);
    }

    /**
     * 验证Token
     * @param msgSignature 签名串，对应URL参数的signature
     * @param timeStamp 时间戳，对应URL参数的timestamp
     * @param nonce 随机串，对应URL参数的nonce
     *
     * @return 是否为安全签名
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public static boolean verifyUrl(String msgSignature, String timeStamp, String nonce,String TOKEN)
            throws AesException {
        // 这里的 WXPublicConstants.TOKEN 填写你自己设置的Token就可以了
        String signature = SHA1.getSHA1(TOKEN, timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        return true;
    }

    /**
     * 获取网页授权凭证
     *
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String requestParam = "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String param = requestParam.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        String response = HttpRequest.sendGet(requestUrl,param);
        logger.info("getOauth2AccessToken:"+response);
        WeixinOauth2Token wat = JSONObject.parseObject(response, WeixinOauth2Token.class);
        return wat;
    }

    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    public static WeixinUserInfo getSNSUserInfo(String accessToken, String openId) {
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo";
        String requestParam = "access_token=ACCESS_TOKEN&openid=OPENID";
        String param  = requestParam.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        String response = HttpRequest.sendGet(requestUrl,param);
        logger.info("getSNSUserInfo:" + response);
        WeixinUserInfo snsUserInfo = JSONObject.parseObject(response, WeixinUserInfo.class);
        return snsUserInfo;
    }
}
