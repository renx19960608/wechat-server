package com.renx.wechatserver.service;



import com.renx.wechatserver.utils.ErrorCode;

import java.util.Map;

public interface WXMenuService {
    /**
     * 创建微信公众号菜单
     * @param response
     * @return
     */
    ErrorCode createMenu(Map<String, Object> response);
}
