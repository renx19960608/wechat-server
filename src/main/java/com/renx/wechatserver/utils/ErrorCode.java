package com.renx.wechatserver.utils;

/**
 * 错误码定义
 */
public enum ErrorCode {

    SUCCEED(0,"succeed"),
    ERR_UNKNOWN(1,"未知错误"),
    ERR_PARAMTER(2,"参数错误"),
    ERR_USERORPWD_INCRECT(1000,"用户或密码错误"),
    ERR_USER_NOTEXIST(1001,"用户不存在"),
	ERR_CAPTCHA_INCRECT(1002,"验证码错误"),
	ERR_OLDPASSWORD_INCRECT(1003,"旧密码错误"),
	ERR_LOGINNAME_REPEAT(1004,"用户名已存在"),
    ERR_VALID_PWD(1005,"登录密码验证失败"),
	
	// 微信公众号相关
	ERR_EMPTY_WXMENU(1101, "没有微信公众号菜单记录"),
    ERR_DELETE_WXMENU(1102, "删除自定义菜单失败"),
    ERR_CREATE_WXMENU(1103, "创建自定义菜单失败"),

    // 第三方接口
    ERR_CHECK_SIGN(1201, "签名验证失败"),

    // 阿里刷脸支付
    ERR_APP_NOT_EXISTS(1301, "应用记录不存在"),
    ERR_ALI_USER_NOT_EXISTS(1302, "用户信息不存在");

    //错误码,0 表示成功
    private int errorCode;

    //错误码对应的描述
    private String errorDesc;

    // 构造方法
    private ErrorCode(int code, String desc) {
        this.errorDesc = desc;
        this.errorCode = code;
    }

    // 普通方法
    public static String getErrorDesc(int code) {
        for (ErrorCode c : ErrorCode.values()) {
            if (c.getErrorCode() == code) {
                return c.errorDesc;
            }
        }
        return null;
    }

    public  String getErrorDesc() {
        return this.errorDesc;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String toString() {
        return this.errorCode+"_"+this.errorDesc;
    }
}
