package com.renx.wechatserver.service.Impl;

import com.alibaba.fastjson.JSON;
import com.renx.commom.redis.RedisClient;
import com.renx.commom.utils.CommonConstant;
import com.renx.wechatserver.dao.WxmenuMapper;
import com.renx.wechatserver.dao.WxuserMapper;
import com.renx.wechatserver.model.WechatMenu;
import com.renx.wechatserver.model.Wxuser;
import com.renx.wechatserver.service.WXUserService;
import com.renx.wechatserver.utils.ErrorCode;
import com.renx.wechatserver.weixin.WeixinUserInfo;
import com.renx.wechatserver.weixin.WxMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WXUserServiceImpl implements WXUserService {
    // 域名
    @Value("${common.domain}")
    private String domain;

    // 公众号配置信息记录id
    @Value("${common.wechat_id}")
    private String wechat_id;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private WxmenuMapper wxmenuMapper;
    @Autowired
    private WxuserMapper wxuserMapper;

    public WXUserServiceImpl() {
    }

    @Override
    public ErrorCode getCurrentUserInfo(String appId, String userId, String openId, Map<String, Object> response) {
        return null;
    }

    @Override
    public ErrorCode userLogin(WeixinUserInfo snsUserInfo, String innerAppid, String outAppid, String menuId, Map<String, Object> resp) {
        Wxuser wxuser=wxuserMapper.selectOne(innerAppid,snsUserInfo.getOpenId());
        boolean existFlag=true;
        if(wxuser==null){
            wxuser=new Wxuser();
            existFlag=false;
        }
        wxuser.setInnerAppid(innerAppid);
        wxuser.setOpenId(snsUserInfo.getOpenId());
        wxuser.setNickName(snsUserInfo.getNickname());// 昵称
        wxuser.setGender(snsUserInfo.getSex() == 1 ? "男" : (snsUserInfo.getSex() == 2 ? "女" : "未知"));// 性别
        wxuser.setCity(snsUserInfo.getCity());// 市
        wxuser.setProvince(snsUserInfo.getProvince());// 省
        wxuser.setCountry(snsUserInfo.getCountry());// 国家
        wxuser.setAvatarUrl(snsUserInfo.getHeadImgUrl());// 头像地址
        wxuser.setUnionId(snsUserInfo.getUnionId());// 微信unionId
        String userId = null;
        if(!existFlag) {// 不存在，需要新增记录
            userId = UUID.randomUUID().toString().replace("-","");
            wxuser.setUserId(userId);
            wxuser.setCreateDate(new Date());
            wxuserMapper.insertSelective(wxuser);
        }else {// 存在，修改记录
            userId = wxuser.getUserId();
            wxuser.setModifyDate(new Date());
            wxuserMapper.updateByPrimaryKeySelective(wxuser);
        }
        String token= UUID.randomUUID().toString();
        token.replace("-","");
        String tokenRedisKey = CommonConstant.USER_TOKEN_REDIS_KEY_PREFIX + token;
        HashMap<String,Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("openId", snsUserInfo.getOpenId());
        userInfo.put("appId", innerAppid);
        String tokenRedisValue = JSON.toJSONString( userInfo );

        redisClient.setex(0, tokenRedisKey,CommonConstant.TOKEN_REDIS_TIME_OUT, tokenRedisValue);

        WechatMenu wxMenu=wxmenuMapper.selectByPrimaryKey(Integer.parseInt(menuId));
        StringBuffer url = new StringBuffer();
        url.append("http://").append(domain).append(wxMenu.getUri());
        url.append("?").append("token=").append(token);
        resp.put("data", url.toString());
        return ErrorCode.SUCCEED;
    }
}
