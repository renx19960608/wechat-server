package com.renx.commom.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Aspect
@Component
public class ControllerLogAdvice {
	private static Logger logger = LoggerFactory.getLogger(ControllerLogAdvice.class);

	@Pointcut("execution(public * com.catic.*.controller.*.*(..))")
	public void controllerLog() {
	}



	/**
	 * 环绕通知：
	 *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
	 *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
	 */
	@Around("controllerLog()")
	public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
		long startTime = System.currentTimeMillis();
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse servletResponse = attributes.getResponse();

		// 记录下请求内容
		Enumeration<String> enums = request.getParameterNames();
		List<String> params = new ArrayList();
		while (enums.hasMoreElements()) {
			String paraName = enums.nextElement();
			String param = paraName + ":" + request.getParameter(paraName);
			params.add(param);
		}
		logger.info("BEGIN: {}, Params: {} ",request.getRequestURI().toString(),params.toString());

		try {//obj之前可以写目标方法执行前的逻辑
			Object obj = proceedingJoinPoint.proceed();//调用执行目标方法
			Object code = null;
			if( obj instanceof  Map ){
				code = ((Map) obj).get("code");
			}
			logger.info("END NORMAL: {},Total Time: {} ms,Result:{}",request.getRequestURI().toString(), System.currentTimeMillis() - startTime,code);
			return obj;
		} catch (Throwable throwable) {
			logger.error("程序异常",throwable);
			Map<String,Object> response = new HashMap<>();
			response.put("code",new Integer(1));
			response.put("message","未知错误");
			servletResponse.setStatus(500);
			logger.error("END EXCEPTION: {},Total Time: {} ms",request.getRequestURI().toString(), System.currentTimeMillis() - startTime);
			return  response;
		}
	}

}
