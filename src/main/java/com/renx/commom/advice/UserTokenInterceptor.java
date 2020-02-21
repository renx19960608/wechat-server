package com.renx.commom.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.renx.commom.redis.RedisClient;
import com.renx.commom.utils.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 前端用户权限验证拦截器
 */
public class UserTokenInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(UserTokenInterceptor.class);

	@Autowired
	private RedisClient redisClient;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if("OPTIONS".equals(request.getMethod().toUpperCase())){
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Headers", "token");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST");
			return true;
		}
		// 获取token数据
		String token = request.getHeader(CommonConstant.USER_REQ_TOKEN);
		if (null == token || "".equals(token)) {
			token = request.getParameter(CommonConstant.USER_REQ_TOKEN);
		}

		if (null == token || "".equals(token)) {
			updateResponse(response);
			return false;
		}
		String tokenRedisKey = CommonConstant.USER_TOKEN_REDIS_KEY_PREFIX+token;
		String tokenRedisValue = redisClient.get(0,tokenRedisKey);
		if (null == tokenRedisValue || "".equals(tokenRedisValue)) {
			updateResponse(response);
			return false;
		}
		HashMap<String,String> userInfo = JSONObject.parseObject(tokenRedisValue,HashMap.class);
		if(userInfo==null || userInfo.size()==0){
			updateResponse(response);
			return false;
		}
		redisClient.expire(0,tokenRedisKey,CommonConstant.TOKEN_REDIS_TIME_OUT);
		for (String key:userInfo.keySet()) {
			request.setAttribute(key,userInfo.get(key));
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "token");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
	}

	private void updateResponse(HttpServletResponse response) throws Exception {
		HashMap<String,Object> respMap = new HashMap<>();
		respMap.put("code",new Integer(999));
		respMap.put("message","无效的Token");

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "token");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		PrintWriter out = response.getWriter();
		out.append(JSON.toJSONString(respMap));
	}
}
