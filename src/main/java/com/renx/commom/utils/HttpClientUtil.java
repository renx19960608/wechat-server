package com.renx.commom.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static String httpReader(String url, String code){
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("GetPage:"+url);
		logger.info("HttpClientUtil.httpReader[" + currentTimeMillis + "]-->url:" + url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);	
		
		String result = null;
		try {
			client.executeMethod(method);
			int status = method.getStatusCode();
			if (status == HttpStatus.SC_OK) {
				result = method.getResponseBodyAsString();
			} else {
				System.out.println("Method failed: " + method.getStatusLine());
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
			logger.error("HttpClientUtil.httpReader[" + currentTimeMillis + "]-->url:" + url + ",出现异常！！！");
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("发生网络异常");
			e.printStackTrace();
			logger.error("HttpClientUtil.httpReader[" + currentTimeMillis + "]-->url:" + url + ",出现异常！！！");
			logger.error(e.getMessage(), e);
		} finally{
			// 释放连接
			if(method!=null)method.releaseConnection();
			method = null;
			client = null;
		}
		logger.info("HttpClientUtil.httpReader[" + currentTimeMillis + "]-->url:" + url + ",result:" + result);
		return result;
	}
	
	/**
	 * get的参数通过map传进来，处理把参数拼接到url的?后面再发起get请求
	 * @param url 不带?及参数的url
	 * @param paramMap 参数map
	 * @return
	 */
	public static String httpGet(String url, Map paramMap) {
		if(paramMap != null && paramMap.size() > 0){
			Iterator it = paramMap.keySet().iterator();
			StringBuffer params = new StringBuffer();
			while (it.hasNext()) {
				String key = it.next() + "";
				Object o = paramMap.get(key);
				if (o != null) {
					params.append(key + "=" + o.toString() + "&");
				}
			}
			params.deleteCharAt(params.length() - 1);
			url = url + "?" + params;
		}
		//编码：UTF-8
		return HttpClientUtil.httpGet(url, "UTF-8");
	}
	
	public static String httpGet(String url,String code) {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("GetPage:"+url);
		logger.info("HttpClientUtil.httpGet[" + currentTimeMillis + "]-->url:" + url);
		String content = null;
		HttpClient httpClient = new HttpClient();
		//设置header
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = httpClient.executeMethod(method);
			System.out.println("httpClientUtils::statusCode="+statusCode);
			System.out.println(method.getStatusLine());
			logger.info("HttpClientUtil.httpGet[" + currentTimeMillis + "]-->url:" + url + ",statusCode:" + statusCode + ",responseBody:" + method.getResponseBody());
			if(statusCode == HttpStatus.SC_OK) {
				content = new String(method.getResponseBody(), code);
			}
		} catch (Exception e) {
			System.out.println("time out");
			e.printStackTrace();
			logger.error("HttpClientUtil.httpGet[" + currentTimeMillis + "]-->url:" + url + ",出现异常！！！");
			logger.error(e.getMessage(), e);
		} finally {
			if(method!=null)method.releaseConnection();
			method = null;
			httpClient = null;
		}
		logger.info("HttpClientUtil.httpGet[" + currentTimeMillis + "]-->url:" + url + ",result:" + content);
		return content;
	}
	
	public static String httpPost(String url, Map paramMap, String code) {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("GetPage:"+url);
		logger.info("HttpClientUtil.httpPost[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap);
		String content = null;
		if (url == null || url.trim().length() == 0 || paramMap == null
				|| paramMap.isEmpty())
			return null;
		HttpClient httpClient = new HttpClient();
		//设置header
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");//
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//		httpClient.getParams().setParameter("User-Agent","Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");
		    //这个是在网上看到的，要加上这个，避免其他错误
	//	httppost.setHeader("Referer", "https://mp.weixin.qq.com");
		//代理设置
		//httpClient.getHostConfiguration().setProxy("128.128.176.74", 808);		
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10 * 1000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(2 * 60 * 1000);
		PostMethod method = new PostMethod(url);
		Iterator it = paramMap.keySet().iterator();
		String params="";
		while (it.hasNext()) {
			String key = it.next() + "";
			Object o = paramMap.get(key);
			if (o != null) {
				method.addParameter(new NameValuePair(key, o.toString()));
				params+=key+"="+ o.toString()+"&";
			}
			if (o != null && o instanceof String[]) {
				String[] s = (String[]) o;
				if (s != null)
					for (int i = 0; i < s.length; i++) {
						method.addParameter(new NameValuePair(key, s[i]));
					}
			}
		}
		try {
			int statusCode = httpClient.executeMethod(method);
			System.out.println("params="+params);
			System.out.println("httpClientUtils::statusCode="+statusCode);
            
			System.out.println(method.getStatusLine());
			logger.info("HttpClientUtil.httpPost[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",statusCode:" + statusCode + ",responseBody:" + method.getResponseBody());
			if(statusCode == HttpStatus.SC_OK) {
				content = new String(method.getResponseBody(), code);
			}
		} catch (Exception e) {
			System.out.println("time out");
			e.printStackTrace();
			logger.error("HttpClientUtil.httpPost[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",出现异常！！！");
			logger.error(e.getMessage(), e);
		} finally {
			if(method!=null)method.releaseConnection();
			method = null;
			httpClient = null;
		}
		logger.info("HttpClientUtil.httpPost[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",result:" + content);
		return content;
	}
	
	public static String[] httpPost2(String url, Map paramMap, String code) {
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("GetPage:"+url);
		logger.info("HttpClientUtil.httpPost2[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap);
		String content = null;
		if (url == null || url.trim().length() == 0 || paramMap == null
				|| paramMap.isEmpty())
			return null;
		HttpClient httpClient = new HttpClient();
		//设置header
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");//
		PostMethod method = new PostMethod(url);
		Iterator it = paramMap.keySet().iterator();
		String params="";
		while (it.hasNext()) {
			String key = it.next() + "";
			Object o = paramMap.get(key);
			if (o != null) {
				method.addParameter(new NameValuePair(key, o.toString()));
				params+=key+"="+ o.toString()+"&";
			}
			if (o != null && o instanceof String[]) {
				String[] s = (String[]) o;
				if (s != null)
					for (int i = 0; i < s.length; i++) {
						method.addParameter(new NameValuePair(key, s[i]));
					}
			}
		}
		String[] strs = new String[2];  
		try {
			int statusCode = httpClient.executeMethod(method);
			System.out.println("params="+params);
			System.out.println("httpClientUtils::statusCode="+statusCode);
            
			System.out.println(method.getStatusLine());
			logger.info("HttpClientUtil.httpPost2[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",statusCode:" + statusCode + ",responseBody:" + method.getResponseBody());
			if(statusCode == HttpStatus.SC_OK) {
				content = new String(method.getResponseBody(), code);
			}
			strs[0]=content;
			strs[1]=url+"?"+params;
		} catch (Exception e) {
			System.out.println("time out");
			e.printStackTrace();
			logger.error("HttpClientUtil.httpPost2[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",出现异常！！！");
			logger.error(e.getMessage(), e);
		} finally {
			if(method!=null)method.releaseConnection();
			method = null;
			httpClient = null;
		}
		logger.info("HttpClientUtil.httpPost2[" + currentTimeMillis + "]-->url:" + url + ",paramMap:" + paramMap + ",result:" + content);
		return strs;
	}
	
	public static String httpPost(String url, Map paramMap) {
		//编码：UTF-8
		return HttpClientUtil.httpPost(url, paramMap, "UTF-8");
	}
	
	/** 
	 * 根据url下载文件，保存到filepath中 
	 * @param url 下载url
	 * @param filepath 保存文件路径
	 * @return true下载成功
	 */
    public static boolean download(String url, String filepath) {
    	long currentTimeMillis = System.currentTimeMillis();
    	logger.info("HttpClientUtil.download[" + currentTimeMillis + "]-->url:" + url + ",filepath:" + filepath);
    	// 是否正常下载保存文件标记，true正常
    	boolean flag = false;
        try {  
        	HttpClient httpClient = new HttpClient();
			GetMethod get = new GetMethod(url);
            httpClient.executeMethod(get);
  
            InputStream is = get.getResponseBodyAsStream();
            File file = new File(filepath);
            if(!file.getParentFile().exists()) {
    			file.getParentFile().mkdirs();
    		}
            FileOutputStream fileout = new FileOutputStream(file);
            /** 
             * 根据实际运行效果 设置缓冲区大小 
             */  
            byte[] buffer=new byte[10 * 1024];
            int ch = 0;  
            while ((ch = is.read(buffer)) != -1) {  
                fileout.write(buffer,0,ch);  
            }
            is.close();
            fileout.flush();
            fileout.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        logger.info("HttpClientUtil.download[" + currentTimeMillis + "]-->url:" + url + ",filepath:" + filepath + ",result:" + flag);
        return flag;
    }
}
