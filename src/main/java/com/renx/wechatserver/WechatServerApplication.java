package com.renx.wechatserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.renx.*.dao")
@ComponentScan({"com.renx.commom","com.renx.wechatserver"})
public class WechatServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatServerApplication.class, args);
	}

}
