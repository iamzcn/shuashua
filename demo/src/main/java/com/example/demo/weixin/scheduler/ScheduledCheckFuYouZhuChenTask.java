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
public class ScheduledCheckFuYouZhuChenTask {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer count0 = 1;
    private Integer count1 = 1;
    private Integer count2 = 1;
//
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
//    }
	
	private boolean isStop0 = false;
	private boolean isStop1 = false;
	private boolean isStop2 = false;
	JSONObject jsonObject = null; 
	
	//@Scheduled(fixedRate = 5000)
	public void checkNumberTask() throws Exception{
		String URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043497";
		//URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105433&unit_id=200011352&dep_id=200043452";
		ArrayList<String> URLs = new ArrayList<String>();
		URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-27&scheduleType=&t=");
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-28&scheduleType=&t=");
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2006322FEZX252270&doctorId=134&regDate=2018-04-27&scheduleType=&t=");
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("曾强27号");
		//names.add("曾强28号");
		//names.add("江文辉27号");
		
		String regEx = "regLeaveCount\":\"[1-9]\"";
		//regEx = "y_state_desc\":\"已约满\"";
		Pattern pattern = Pattern.compile(regEx);
		
		for(int j=0;j<URLs.size();j++) {
			String radomStr = String.valueOf(Math.random());
			if(!isStop0) {
				System.out.println(String.format(names.get(j) + "---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
				//if(count0 == 2) return;
				if(count0 == 2) continue;
				try {
					String ret = HttpPostUtil.doHttpPostJson(URLs.get(j) + radomStr, "");
					
					//jsonObject = JSONObject.fromObject(ret);
					//if(count0 % 70 == 8) System.out.println("["+dateFormat.format(new Date()) + "]" + ret);
					//System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + ret);
					
		//			JSONArray jsonArray = (JSONArray)((JSONObject)jsonObject.get("data")).get("schList");
		//			if(jsonArray.size() > 0) {
		//				for(int i=0;i<jsonArray.size();i++) {
		//					jsonArray.get(i);
		//				}
		//			}
					//System.out.println(jsonObject);
					Matcher matcher = pattern.matcher(ret);
					boolean rs = matcher.find();
					//System.out.println(rs);
					if(rs) {
						isStop0 = true;
						System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + "有号啦");
						Template tem=new Template();  
						tem.setTemplateId("nM6Kj6ux7e6XHfVMwzzQbqbTjkQOjzltWblTn4lyOBw");  
						tem.setTopColor("#00DD00");  
						tem.setToUser("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");  
						tem.setUrl("");  
						          
						List<TemplateParam> paras=new ArrayList<TemplateParam>();  
						paras.add(new TemplateParam("first","有号啦","#FF3333"));  
						paras.add(new TemplateParam("keyword1", names.get(j) + "有号啦","#0044BB"));  
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
	
	//@Scheduled(fixedRate = 5000)
	public void checkNumberTask1() throws Exception{
		String URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043497";
		//URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105433&unit_id=200011352&dep_id=200043452";
		ArrayList<String> URLs = new ArrayList<String>();
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-27&scheduleType=&t=");
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-28&scheduleType=&t=");
		URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2006322FEZX252270&doctorId=134&regDate=2018-04-27&scheduleType=&t=");
		
		ArrayList<String> names = new ArrayList<String>();
		//names.add("曾强27号");
		//names.add("曾强28号");
		names.add("江文辉27号");
		
		String regEx = "regLeaveCount\":\"[1-9]\"";
		//regEx = "y_state_desc\":\"已约满\"";
		Pattern pattern = Pattern.compile(regEx);
		
		for(int j=0;j<URLs.size();j++) {
			String radomStr = String.valueOf(Math.random());
			if(!isStop1) {
				System.out.println(String.format(names.get(j) + "---第%s次执行，当前时间为：%s", count1++, dateFormat.format(new Date())));
				//if(count0 == 2) return;
				if(count1 == 2) continue;
				try {
					String ret = HttpPostUtil.doHttpPostJson(URLs.get(j) + radomStr, "");
					
					//jsonObject = JSONObject.fromObject(ret);
					//if(count0 % 70 == 8) System.out.println("["+dateFormat.format(new Date()) + "]" + ret);
					//System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + ret);
					
		//			JSONArray jsonArray = (JSONArray)((JSONObject)jsonObject.get("data")).get("schList");
		//			if(jsonArray.size() > 0) {
		//				for(int i=0;i<jsonArray.size();i++) {
		//					jsonArray.get(i);
		//				}
		//			}
					//System.out.println(jsonObject);
					Matcher matcher = pattern.matcher(ret);
					boolean rs = matcher.find();
					//System.out.println(rs);
					if(rs) {
						isStop1 = true;
						System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + "有号啦");
						Template tem=new Template();  
						tem.setTemplateId("nM6Kj6ux7e6XHfVMwzzQbqbTjkQOjzltWblTn4lyOBw");  
						tem.setTopColor("#00DD00");  
						tem.setToUser("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");  
						tem.setUrl("");  
						          
						List<TemplateParam> paras=new ArrayList<TemplateParam>();  
						paras.add(new TemplateParam("first","有号啦","#FF3333"));  
						paras.add(new TemplateParam("keyword1", names.get(j) + "有号啦","#0044BB"));  
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
	
	//@Scheduled(fixedRate = 5000)
	public void checkNumberTask2() throws Exception{
		String URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043497";
		//URL = "https://wap.91160.com/doctor/schedule.html?doctor_id=200105433&unit_id=200011352&dep_id=200043452";
		ArrayList<String> URLs = new ArrayList<String>();
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-27&scheduleType=&t=");
		URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2003000FEZX239892&doctorId=666&regDate=2018-04-28&scheduleType=&t=");
		//URLs.add("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?hospitalId=100202&deptId=2006322FEZX252270&doctorId=134&regDate=2018-04-27&scheduleType=&t=");
		
		ArrayList<String> names = new ArrayList<String>();
		//names.add("曾强27号");
		names.add("曾强28号");
		//names.add("江文辉27号");
		
		String regEx = "regLeaveCount\":\"[1-9]\"";
		//regEx = "y_state_desc\":\"已约满\"";
		Pattern pattern = Pattern.compile(regEx);
		
		for(int j=0;j<URLs.size();j++) {
			String radomStr = String.valueOf(Math.random());
			if(!isStop2) {
				System.out.println(String.format(names.get(j) + "---第%s次执行，当前时间为：%s", count2++, dateFormat.format(new Date())));
				//if(count0 == 2) return;
				if(count2 == 2) continue;
				try {
					String ret = HttpPostUtil.doHttpPostJson(URLs.get(j) + radomStr, "");
					
					//jsonObject = JSONObject.fromObject(ret);
					//if(count0 % 70 == 8) System.out.println("["+dateFormat.format(new Date()) + "]" + ret);
					//System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + ret);
					
		//			JSONArray jsonArray = (JSONArray)((JSONObject)jsonObject.get("data")).get("schList");
		//			if(jsonArray.size() > 0) {
		//				for(int i=0;i<jsonArray.size();i++) {
		//					jsonArray.get(i);
		//				}
		//			}
					//System.out.println(jsonObject);
					Matcher matcher = pattern.matcher(ret);
					boolean rs = matcher.find();
					//System.out.println(rs);
					if(rs) {
						isStop2 = true;
						System.out.println("["+dateFormat.format(new Date()) + "]" + names.get(j) + "有号啦");
						Template tem=new Template();  
						tem.setTemplateId("nM6Kj6ux7e6XHfVMwzzQbqbTjkQOjzltWblTn4lyOBw");  
						tem.setTopColor("#00DD00");  
						tem.setToUser("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");  
						tem.setUrl("");  
						          
						List<TemplateParam> paras=new ArrayList<TemplateParam>();  
						paras.add(new TemplateParam("first","有号啦","#FF3333"));  
						paras.add(new TemplateParam("keyword1", names.get(j) + "有号啦","#0044BB"));  
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
