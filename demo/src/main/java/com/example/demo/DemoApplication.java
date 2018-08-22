package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.demo.pojo.Constant;
import com.example.weixin.pojo.AccessToken;
import com.example.weixin.util.AccessTokenUtil;
import com.example.weixin.util.MenuUtil;

import net.sf.json.JSONObject;
@ComponentScan 
@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
@EnableTransactionManagement
public class DemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		
		AccessTokenUtil.getAccessToken(Constant.appId, Constant.appSecret);
		
		JSONObject fromObject = JSONObject.fromObject(MenuUtil.initMenu());
        int result = MenuUtil.createMenu(fromObject.toString(), AccessToken.getInstance().getToken());
        if (result == 0) {
            System.out.println("create menu success");
        } else {
            System.out.println("error code : " + result);
        }
        
	}
}
