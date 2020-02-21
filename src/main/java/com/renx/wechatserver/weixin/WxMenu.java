package com.renx.wechatserver.weixin;


/**
 * 微信菜单
 * @author YUXY
 *
 */
public class WxMenu {

    private String name;// 菜单标题
    private String type;// view表示网页类型，click表示点击类型，miniprogram表示小程序类型
    private String url;// （view、miniprogram类型必须）网页 链接，用户点击菜单可打开链接
    private String key;// （click等点击类型必须）菜单KEY值，用于消息接口推送

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
