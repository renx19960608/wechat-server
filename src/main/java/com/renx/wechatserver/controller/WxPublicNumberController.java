package com.renx.wechatserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.renx.commom.redis.RedisClient;
import com.renx.commom.utils.CommonConstant;
import com.renx.wechatserver.dao.ConfigMapper;
import com.renx.wechatserver.model.WechatConfig;
import com.renx.wechatserver.service.WXMenuService;
import com.renx.wechatserver.service.WXUserService;
import com.renx.wechatserver.utils.ErrorCode;
import com.renx.wechatserver.weixin.WXPublicUtils;
import com.renx.wechatserver.weixin.WeixinOauth2Token;
import com.renx.wechatserver.weixin.WeixinUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class WxPublicNumberController {
    private final static Logger logger = LoggerFactory.getLogger(WxPublicNumberController.class);
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private WXMenuService wxMenuService;
    @Autowired
    private WXUserService wxUserService;
    @Autowired
    private RedisClient redisClient;
    @Value("${common.wechat_id}")
    private String wechat_id;
    @Value("${common.domain}")
    private String domain;
    /**
     * 公众号验签
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wechat-server/wechat/001/wechatPublishEvent",method = RequestMethod.GET)
    public void verifyWXToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        // 查询获取微信公众号的token
        WechatConfig config = configMapper.getConfigByWechatid(wechat_id);
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce,config.getToken())) {
            out.print(echostr);
            out.flush();
        }
    }
    /**
     * 从数据库中读取微信菜单信息，并且调用微信接口创建菜单,我们系统的菜单的url比较特别，必须指向/sysmgr-server/wechat/001/redirect/{menuId}
     * {
     "type":"view",
     "name":"就诊中心",
     "url":""
     }
     *
     */
    @ResponseBody
    @RequestMapping(value = "/sysmgr-server/web/wechartManage/001/createMenu")
    public Map<String, Object> createMenu() {
        Map<String, Object> response = new HashMap<String,Object>();
        ErrorCode retCode=wxMenuService.createMenu(response);
        response.put("code", retCode.getErrorCode());
        response.put("message",retCode.getErrorDesc());
        return response;
    }
    /**
     *  微信公众号上面的菜单的跳转地址都指向这里
     */
    @RequestMapping(value = "/sysmgr-server/wechat/001/redirect/{menuId}")
    public void redirectMenu(@PathVariable("menuId") String menuId,HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 取出所有参数
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> parameterNames=request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String parameterName=parameterNames.nextElement();
            params.put(parameterName,request.getParameter(parameterName));
        }
        String randomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String redisKey = CommonConstant.REQ_PARAMS_REDIS_KEY_PREFIX + randomCode;
        if(params.size()>0){
            // 把请求参数缓存到redis
            redisClient.setex(0, redisKey, CommonConstant.REQ_PARAMS_REDIS_TIMEOUT_OUT, JSON.toJSONString(params));
        }
        StringBuffer sb=new StringBuffer();
        sb.append("http://").append(domain).append("/sysmgr-server/wechat/001/oauth2/").append(menuId).append("/").append(randomCode);
        String redirectUri = URLEncoder.encode(sb.toString(), "utf-8");
        WechatConfig config=configMapper.getConfigByWechatid(wechat_id);
        String url= "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + config.getAppid() + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        response.sendRedirect(url);
    }
    /**
     * 所有微信公众号上的菜单，点击后都会跳转到本接口进行授权，在这里获取微信用户信息(存库)，分配token，根据菜单ID跳转到对应的菜单
     */
    @RequestMapping(value = "/sysmgr-server/wechat/001/oauth2/{menuId}/{randomCode}")
    public void wxPublicNumberOauth(@PathVariable("menuId") String menuId, @PathVariable("randomCode") String randomCode, String code, String state, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        // 用户同意授权后，能获取到code
        logger.info("wxPublicNumberOauth【{},{},{}】......", code, state, menuId);
        // 用户同意授权
        try{
            WechatConfig config=configMapper.getConfigByWechatid(wechat_id);
            WeixinOauth2Token oauth2Token=WXPublicUtils.getOauth2AccessToken(config.getAppid(),config.getSecret(),code);
            WeixinUserInfo snsUserInfo=WXPublicUtils.getSNSUserInfo(oauth2Token.getAccessToken(),oauth2Token.getOpenId());
            Map<String, Object> resp = new HashMap<String, Object>();
            // 微信用户登录，分配token
            ErrorCode retCode = wxUserService.userLogin(snsUserInfo, wechat_id, config.getAppid(), menuId, resp);
            logger.info("wxUserService.userLogin({},{},{}):{},{}", wechat_id, config.getAppid(), menuId, retCode.toString(), resp);
            if(retCode.equals(ErrorCode.SUCCEED)) {
                String url = (String) resp.get("data");
                // 从redis查randomCode对应的参数并拼接到url尾部
                String paramsStr = redisClient.get(0, CommonConstant.REQ_PARAMS_REDIS_KEY_PREFIX + randomCode);
                if(!StringUtils.isEmpty(paramsStr)){
                    HashMap<String, String> paramMap = JSONObject.parseObject(paramsStr, HashMap.class);
                    Set<String> keySet = paramMap.keySet();
                    StringBuffer sb = new StringBuffer(url);
                    for (String key : keySet) {
                        sb.append("&").append(key).append("=").append(paramMap.get(key));
                    }
                    url = sb.toString();
                }
                // 带上token前转到指定地址
                response.sendRedirect(url);
            }
        }catch (Exception e){
            logger.error("wxPublicNumberOauth错误",e);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/test")
    public void test(HttpServletResponse response) throws IOException{
        response.sendRedirect("http://renx-123.ngrok2.xiaomiqiu.cn/index.html");
    }
}
