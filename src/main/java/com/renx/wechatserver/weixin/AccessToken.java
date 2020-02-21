package com.renx.wechatserver.weixin;

public class AccessToken {
    int errcode;

    String errmsg;

    String access_token;

    int expires_in;
    
    /**
     * 为了维护全局AccessToken新增的字段
     */
 	// 失效时间
 	private long invalidTime;
 	
 	public void setInvalidTime() {
		// 假设微信规定120分钟有效期,为保证稳定, 我们 110 分钟时就重新获取
		this.invalidTime = expires_in * 1000 + System.currentTimeMillis() - (1000 * 60 * 10);
 	}

    public long getInvalidTime() {
		return invalidTime;
	}
    
    public boolean isValid() {
		return invalidTime >= System.currentTimeMillis();
	}

	public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
