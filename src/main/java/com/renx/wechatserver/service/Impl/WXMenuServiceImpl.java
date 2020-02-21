package com.renx.wechatserver.service.Impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renx.wechatserver.dao.ConfigMapper;
import com.renx.wechatserver.dao.WxmenuMapper;
import com.renx.wechatserver.model.WechatConfig;
import com.renx.wechatserver.model.WechatMenu;
import com.renx.wechatserver.service.WXMenuService;
import com.renx.wechatserver.utils.ErrorCode;
import com.renx.wechatserver.weixin.WxMenUtil;
import com.renx.wechatserver.weixin.WxMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
@Service
public class WXMenuServiceImpl implements WXMenuService {
    @Value("${common.domain}")
    private String domain;
    @Value("${common.wechat_id}")
    private String wechat_id;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private WxmenuMapper wxmenuMapper;
    private WxMenu transferMenuInfo(WechatMenu menu){
        WxMenu wxMenu = new WxMenu();
        wxMenu.setName(menu.getName());
        if(menu.getMenuType() == 1) {// 网页
            wxMenu.setType("view");
        }
        if(menu.getLevel() == -1) {// 外部链接
            wxMenu.setUrl(menu.getUri());
        }else if(menu.getLevel() == 0) {// 内部链接
            StringBuffer sb = new StringBuffer();
            sb.append("http://").append(domain).append("/sysmgr-server/wechat/001/redirect/").append(menu.getMenuId());
            // 内部链接指向地址
            wxMenu.setUrl(sb.toString());
        }
        return wxMenu;
    }
    @Override
    public ErrorCode createMenu(Map<String, Object> response) {
        List<WechatMenu> wechatMenuList=wxmenuMapper.selectByGTLevel(wechat_id,0);
        //一级菜单
        List<WechatMenu> topMenus=new ArrayList<>();
        //二级菜单
        Map<Integer, List<WxMenu>> subMenusMap = new HashMap<Integer, List<WxMenu>>();
        for(WechatMenu wechatMenu:wechatMenuList){
            WxMenu wxMenu=transferMenuInfo(wechatMenu);
            if(wechatMenu.getParentId()==0){
                topMenus.add(wechatMenu);
            }else{
                List<WxMenu> list = subMenusMap.get(wechatMenu.getParentId());
                if(list == null) {
                    list = new ArrayList<WxMenu>();
                    subMenusMap.put(wechatMenu.getParentId(), list);
                }
                list.add(wxMenu);
            }
        }
        // 组装公众号菜单json数据
        JSONArray jsonArray = new JSONArray();
        for(WechatMenu wechatMenu:topMenus){
            List<WxMenu> list = subMenusMap.get(wechatMenu.getMenuId());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",wechatMenu.getName());
            if(list != null && list.size() > 0) {
                jsonObject.put("sub_button", JSONArray.toJSON(list));
            }else {
                jsonObject.put("sub_button", new JSONArray());
                // 可能一级菜单直接是个跳转链接，这里增加针对这种情况的处理
                if(!StringUtils.isEmpty(wechatMenu.getUri())){
                    WxMenu wxMenu = transferMenuInfo(wechatMenu);
                    jsonObject.put("type", wxMenu.getType());
                    jsonObject.put("url", wxMenu.getUrl());
                }
            }
            jsonArray.add(jsonObject);
        }
        if(jsonArray.size()>0){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("button",jsonArray);
            WechatConfig config=configMapper.getConfigByWechatid(wechat_id);
            System.out.println("菜单json"+jsonObject.toString());
            if(WxMenUtil.createMenu(config.getAppid(),config.getSecret(),jsonObject.toString())){
                return ErrorCode.SUCCEED;
            }else {
                return ErrorCode.ERR_CREATE_WXMENU;
            }
        }else{
            return ErrorCode.ERR_EMPTY_WXMENU;
        }

    }
}
