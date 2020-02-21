package com.renx.wechatserver.service;




import com.renx.wechatserver.utils.ErrorCode;
import com.renx.wechatserver.weixin.WeixinUserInfo;

import java.util.Map;

/**
 * @author liuqiongyu
 * @create 2019-01-17
 * @description
 */
public interface WXUserService {

    /*****
     * 获取当前用户信息
     * @param appId 内部应用标识
     * @param userId 用户标识
     * @param openId 微信的OPENID
     * @param response 响应数据 response["data"] = 响应报文的JSON串
     * @return ErrorCode 处理结果码，成功 OR 其他错误
     */
    public ErrorCode getCurrentUserInfo(String appId, String userId, String openId, Map<String, Object> response);

    /**
     * 微信公众号用户登录，分配token
     * @param snsUserInfo 用户信息
     * @param innerAppid 内部APPID
     * @param outAppid 微信appID
     * @param menuId 菜单id
     * @param resp
     * @return
     */
	public ErrorCode userLogin(WeixinUserInfo snsUserInfo, String innerAppid, String outAppid, String menuId, Map<String, Object> resp);

}
