package com.example.demo.weixin.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.weixin.pojo.AccessToken;
import com.example.weixin.pojo.Template;
import com.example.weixin.pojo.TemplateParam;
import com.example.weixin.util.HttpPostUtil;
import com.example.weixin.util.Sendmsg;

import net.sf.json.JSONObject;

@Component
public class ScheduledCheckShenZhongYiTask {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer count0 = 1;
//
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
//    }
	
	private boolean isStop = false;
	JSONObject jsonObject = null; 
	
	//@Scheduled(fixedRate = 5000)
	public void checkNumberTask() throws Exception{
		String URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043497";
		//URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105433&unit_id=200011352&dep_id=200043452";
		ArrayList<String> URLs = new ArrayList<String>();
//		URLs.add("https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043497");
//		URLs.add("https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043500");
		URLs.add("https://wap.91160.com/doctor/schedule.html?unit_id=200011353&doctor_ids=200104961");
		
		
		String regEx = "y_state\":\"1\"";
		//regEx = "y_state_desc\":\"已约满\"";
		Pattern pattern = Pattern.compile(regEx);
		
		for(int j=0;j<URLs.size();j++) {
		
			if(!isStop) {
				//System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
				//if(count0 == 2) return;
				count0++;
				if(count0 == 2) continue;
				try {
					String ret = HttpPostUtil.doHttpPostJson(URLs.get(j), "");
					
					jsonObject = JSONObject.fromObject(ret);
					if(count0 % 70 == 8) System.out.println("["+dateFormat.format(new Date()) + "]" + jsonObject);
					
		//			JSONArray jsonArray = (JSONArray)((JSONObject)jsonObject.get("data")).get("schList");
		//			if(jsonArray.size() > 0) {
		//				for(int i=0;i<jsonArray.size();i++) {
		//					jsonArray.get(i);
		//				}
		//			}
					//System.out.println(jsonObject);
					Matcher matcher = pattern.matcher(jsonObject.toString());
					boolean rs = matcher.find();
					//System.out.println(rs);
					if(rs) {
						isStop = true;
						System.out.println(jsonObject);
						Template tem=new Template();  
						tem.setTemplateId("nM6Kj6ux7e6XHfVMwzzQbqbTjkQOjzltWblTn4lyOBw");  
						tem.setTopColor("#00DD00");  
						tem.setToUser("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");  
						tem.setUrl("");  
						          
						List<TemplateParam> paras=new ArrayList<TemplateParam>();  
						paras.add(new TemplateParam("first","有号啦","#FF3333"));  
						paras.add(new TemplateParam("keyword1","林季文有号啦","#0044BB"));  
						paras.add(new TemplateParam("remark","Remark","#AAAAAA"));  
						          
						tem.setTemplateParamList(paras);  
						String accessToken = AccessToken.getInstance().getToken();
						Sendmsg.sendTemplateMsg(accessToken,tem);
						
						tem.setToUser("oeM4n1EWCOuxsjizk_mM2DBRBqV0");  
						Sendmsg.sendTemplateMsg(accessToken,tem);
						
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

}
