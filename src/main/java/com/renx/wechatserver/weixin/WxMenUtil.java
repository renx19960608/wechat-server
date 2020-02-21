package com.renx.wechatserver.weixin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
public class WxMenUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxMenUtil.class);

    private static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete";
    private static String MENU_DELETE_PARAM = "access_token={accessToken}";

    private static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={accessToken}";

    /**
     * 删除自定义菜单
     * @param appId
     * @param appSecret
     * @return
     */
    public static boolean deleteMenu(String appId, String appSecret) {
        // 调用接口获取access_token
        AccessToken accessToken = AccessTokenUtil.getAccessToken(appId, appSecret);
        if(accessToken != null) {
            String param = MENU_DELETE_PARAM.replace("{accessToken}", accessToken.getAccess_token());
            String deleteMenuJsonStr = HttpRequest.sendGet(MENU_DELETE_URL, param);
            logger.info("自定义菜单删除返回结果：" + deleteMenuJsonStr);
            JSONObject jsonObject = JSONObject.parseObject(deleteMenuJsonStr);
            if(jsonObject.getInteger("errcode") == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建自定义菜单
     * @param appId
     * @param appSecret
     * @param menus
     * @return
     */
    public static boolean createMenu(String appId, String appSecret, String menus) {
        // 调用接口获取access_token
        AccessToken accessToken = AccessTokenUtil.getAccessToken(appId, appSecret);
        if(accessToken != null) {
            String url = MENU_CREATE_URL.replace("{accessToken}", accessToken.getAccess_token());
            String createMenuJsonStr = HttpRequest.sendPost(url, menus);
            logger.info("自定义菜单创建返回结果：" + createMenuJsonStr + "，菜单json：" + menus);
            JSONObject jsonObject = JSONObject.parseObject(createMenuJsonStr);
            if(jsonObject.getInteger("errcode") == 0) {
                return true;
            }
        }
        return false;
    }

}
