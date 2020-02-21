package com.renx.wechatserver.weixin;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenUtil.class);

    private static String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token";
    private static String ACCESSTOKENPARAM = "grant_type=client_credential&appid={appId}&secret={appSecret}";

    // 全局AccessToken，因微信限200（次/天），需要做控制
    private static AccessToken accessToken = null;

    /**
     * 获取access_Token
     *
     * @return
     */
    public static AccessToken getAccessToken(String appId, String appSecret) {
        if (accessToken == null || !accessToken.isValid()) {
            accessToken = null;

            String param = ACCESSTOKENPARAM.replace("{appId}", appId).replace("{appSecret}", appSecret);
            String accessTokenJsonStr = HttpRequest.sendGet(ACCESSTOKENURL, param);
            logger.info("getAccessToken:" + accessTokenJsonStr);
            AccessToken tmpAccessToken = JSONObject.parseObject(accessTokenJsonStr, AccessToken.class);
            if(tmpAccessToken.getErrcode() == 0) {// 请求成功
                accessToken = tmpAccessToken;
                // 获取到新的AccessToken后，维护失效时间
                accessToken.setInvalidTime();
            }
        }
        return accessToken;
    }

}
