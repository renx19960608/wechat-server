package com.renx.wechatserver;

import com.alibaba.fastjson.JSONObject;
import com.renx.wechatserver.dao.ConfigMapper;
import com.renx.wechatserver.model.WechatConfig;
import com.renx.wechatserver.weixin.WxMenu;

//import org.junit.Test;

import javax.annotation.Resource;
import java.util.*;


class WechatServerApplicationTests {
	public static void main(String args[]){
		Map<Integer, List<String>> subMenusMap = new HashMap<Integer, List<String>>();
		List<String> list = subMenusMap.get(0);
		if(list == null) {
			list = new ArrayList<String>();
			subMenusMap.put(0, list);
		}
		list.add("asasa");
		System.out.print(JSONObject.toJSON(subMenusMap).toString());
	}
}
