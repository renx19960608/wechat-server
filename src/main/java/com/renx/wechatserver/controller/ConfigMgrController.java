package com.renx.wechatserver.controller;

import com.renx.wechatserver.dao.ConfigMapper;
import com.renx.wechatserver.model.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigMgrController {
    @Autowired
    ConfigMapper configMapper;

    @RequestMapping("/get")
    public WechatConfig getconfig(){
        return configMapper.getConfigByWechatid("renx");
    }

    @RequestMapping("/insert")
    public int insertconfig(){
        WechatConfig config=new WechatConfig();
        config.setAppid("appid");
        config.setSecret("secret");
        config.setWechat_id("szetyy");
        return configMapper.insertConfig(config);
    }
    @RequestMapping("/update")
    public int updateConfig(){
        WechatConfig config=new WechatConfig();
        config.setAppid("appid01");
        config.setWechat_id("renx");
        return configMapper.updateconfig(config);
    }
    @RequestMapping("/del")
    public int delconfig(){
        return configMapper.delconfg("szetyy");
    }
}
