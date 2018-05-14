package com.example.demo.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.SourceConfigRepository;
import com.example.demo.dao.TaskRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.SourceConfig;
import com.example.demo.pojo.SourceParameter;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.User;
import com.example.demo.util.TaskUtil;
import com.example.weixin.pojo.TemplateParam;

@RestController
public class RESTController {
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private SourceConfigRepository sourceRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	private final Logger log = LoggerFactory.getLogger(RESTController.class);
	
	
	@RequestMapping(value = "/checkNumber", method = RequestMethod.POST)  
	public void test(@RequestParam("date") String date) {  
		Task task = new Task();
		
		try {
			
			
			
			SourceConfig sourceConfig = sourceRepo.findById("5ae8179ce97b37317019a3c7");
			User user = userRepo.findByOpenId("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");
			
//			sourceConfig.setBaseURL("http://www.benewit.cn/fyfwh-web/public/getDrRegTimeInfo001?");
//			sourceConfig.setMonRegEx("regLeaveCount\\\":\\\"[1-9]\\\"");
//			sourceConfig.setParameters("hospitalId&deptId&doctorId&regDate&scheduleType&t");
//			sourceConfig.setSourceFrom("微信公众号");
//			sourceConfig.setSourceKey("妇女儿童医疗中心");
			 List<SourceParameter> spList = new ArrayList<SourceParameter>();
//			 spList.add(new SourceParameter("医生","曾强","doctorId","666","Y", "string"));
//			 spList.add(new SourceParameter("医院","妇女儿童医疗中心","hospitalId","100202","Y", "string"));
//			 spList.add(new SourceParameter("科室","儿童呼吸科","deptId","2003000FEZX239892","Y", "string"));
//			 //spList.add(new SourceParameter("日期(YYYY-MM-DD)","日期","regDate","2018-05-03","Y"));
//			 spList.add(new SourceParameter("日期(YYYY-MM-DD)","日期","regDate",date,"Y", "date"));
//			 
//			 spList.add(new SourceParameter("scheduleType","scheduleType","scheduleType","","N", "string"));
//			 Double r = Math.random();
//			 spList.add(new SourceParameter("random number","t","t",String.valueOf(r),"N", "string"));
//			 sourceConfig.setSourceParameter(spList);
//			 sourceConfig.setSourceType("doctor");
//			 sourceConfig.setWxMsgTemplateId("7fIUNZ88HohE3ekYBjEai0NjJxq6FdnY5UeK9ruJghQ");
//			 
//			 List<TemplateParam> tpList = new ArrayList<TemplateParam>();
//			 tpList.add(new TemplateParam("first", "刷到号源啦", "#FF3333"));
//			 tpList.add(new TemplateParam("keyword1", "您的刷刷：{sourceParameter.doctorId.name} {sourceParameter.hospitalId.name} {sourceParameter.deptId.name} {sourceParameter.regDate.value} 有号源可约啦，赶紧进行预约吧！", "#0044BB"));
//			 tpList.add(new TemplateParam("remark", "感谢您使用刷刷！", "#AAAAAA"));
//			 sourceConfig.setWxMsgSuccessValue(tpList);
//			 
//			 List<TemplateParam> tpStartList = new ArrayList<TemplateParam>();
//			 tpStartList.add(new TemplateParam("first", "刷刷开始啦", "#FF3333"));
//			 tpStartList.add(new TemplateParam("keyword1", "您的刷刷：{sourceParameter.doctorId.name} {sourceParameter.hospitalId.name} {sourceParameter.deptId.name} {sourceParameter.regDate.value} 开始啦，请留意公众号的提醒！", "#0044BB"));
//			 tpStartList.add(new TemplateParam("remark", "感谢您使用刷刷！", "#AAAAAA"));
//			 sourceConfig.setWxMsgStartValue(tpStartList);
//			 
//			 List<TemplateParam> tpErrorList = new ArrayList<TemplateParam>();
//			 tpErrorList.add(new TemplateParam("first", "刷刷结束啦", "#FF3333"));
//			 tpErrorList.add(new TemplateParam("keyword1", "您的刷刷：{sourceParameter.doctorId.name} {sourceParameter.hospitalId.name} {sourceParameter.deptId.name} {sourceParameter.regDate.value} 结束啦，原因：{customMessage}", "#0044BB") );
//			 tpErrorList.add(new TemplateParam("remark", "感谢您使用刷刷！", "#AAAAAA"));
//			 sourceConfig.setWxMsgErrorValue(tpErrorList);
//			 
//			
//			 user.setInterval(3000);
//			 user.setTryCount(20);
//			 user.setWxId("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");
			
			
			 
			 
			 task.setAmendedTime(new Date());
			 task.setStatus(Constant.TS_THREADED);
			 task.setCreatedTime(new Date());
			 
			 task.setBaseURL(sourceConfig.getBaseURL());
			 task.setWxMsgTemplateId(sourceConfig.getWxMsgTemplateId());
			 task.setMonRegEx(sourceConfig.getMonRegEx());
			 task.setParameters(sourceConfig.getParameters());
			 task.setSourceFrom(sourceConfig.getSourceFrom());
			 task.setSourceKey(sourceConfig.getSourceKey());
			 task.setSourceType(sourceConfig.getSourceType());
			 
			 task.setInterval(user.getInterval());
			 task.setTryCount(user.getTryCount());
			 task.setOpenId(user.getOpenId());
			 
			 
			 /* TODO Convert sourceParameter to taskParameter
			  */
			 task.setTaskParameter(spList);
			 
			 /* TODO
			  * Convert sourceWxMsgSuccessValue to taskWxMsgSuccessValue
			  */
			 task.setWxTaskMsgSuccessValue(sourceConfig.getWxMsgSuccessValue());
			 TaskUtil.buildWxTaskMsgValue(task, "SUCCESS", null);
			 /* TODO
			  * Convert sourceWxMsgErrorValue to taskWxMsgErrorValue
			  */
			 task.setWxTaskMsgErrorValue(sourceConfig.getWxMsgErrorValue());
			 TaskUtil.buildWxTaskMsgValue(task, "ERROR", null);
			 /* TODO
			  * Convert sourceWxMsgStartValue to taskWxMsgStartValue
			  */
			 task.setWxTaskMsgStartValue(sourceConfig.getWxMsgStartValue());
			 TaskUtil.buildWxTaskMsgValue(task, "START", null);
			 
			 
			 
			 TaskUtil.buildURLParameters(task);
			 
			 taskRepo.save(task);
			 
			 log.info("New task " + task.toString() + " is created.");
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Task is not created due to error : " + e.getMessage());
			
		}
		
		
	}
	
	
	
	@RequestMapping(value = "/cancelAllByUser", method = RequestMethod.POST)  
	public void cancel() {  
		try {
			
			List<Task> task = taskRepo.findByOpenId("oeM4n1D0CwGMiEbat7BhRtQ7wE8A");
			for(int i=0;i<task.size();i++) {
				Task t = task.get(i);
				t.setStatus(Constant.TS_USER_CANCELLED);
				t.setAmendedTime(new Date());
				TaskUtil.save(taskRepo, t);
				log.info(t.toString() + " is cancelled by user.");
			}
			
			//taskRepo.save(task);
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Task is not cancelled due to error : " + e.getMessage());
			
		}
		
		
	}
	
	
	
	
}
