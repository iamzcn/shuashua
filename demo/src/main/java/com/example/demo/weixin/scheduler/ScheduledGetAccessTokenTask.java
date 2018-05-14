package com.example.demo.weixin.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.pojo.Constant;
import com.example.weixin.util.AccessTokenUtil;

@Component
public class ScheduledGetAccessTokenTask {
//	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private Integer count0 = 1;
//
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
//    }
    
    @Scheduled(fixedRate = 7000000)
    public void getAccessTokenTask() throws Exception{
    	//AccessTokenUtil.getAccessToken("wx35f4ea5f815f82c0", "2b6210fa876f349e7dfb7e8c6113d626");
    	AccessTokenUtil.getAccessToken(Constant.appId, Constant.appSecret);
    }
}
