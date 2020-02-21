package com.renx.wechatserver.dao;

import com.renx.wechatserver.model.WechatConfig;
import org.apache.ibatis.annotations.Select;

public interface ConfigMapper {
    public WechatConfig getConfigByWechatid(String wechat_id);
    public int insertConfig(WechatConfig config);
    public int updateconfig(WechatConfig config);
    public int delconfg(String wechat_id);
}
