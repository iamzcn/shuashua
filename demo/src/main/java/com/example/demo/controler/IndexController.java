package com.example.demo.controler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.SourceConfigRepository;
import com.example.demo.dao.TaskRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.HTMLSelectOption;
import com.example.demo.pojo.SourceConfig;
import com.example.demo.pojo.SourceParameter;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.User;
import com.example.demo.util.TaskUtil;
import com.example.weixin.pojo.WXUser;
import com.example.weixin.util.UserInfoUtil;

@Controller
public class IndexController {
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private SourceConfigRepository scRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	private final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	private final static Map<String, String> urlMapping;
	
	static
	{
		urlMapping = new HashMap<String, String>();
		urlMapping.put("home", "http://iamz.free.ngrok.cc/doctor/home");  
        urlMapping.put("myShuaShua", "http://iamz.free.ngrok.cc/doctor/myShuaShua"); 
	}
	
	private String shuaSubmitInfo = "shuaSubmitInfo";
	private String myShuaShua = "myShuaShua";
	
	@RequestMapping("/doctor/home")  
    String home(Model model, HttpSession session,
			@RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state) {  
		
		String infoMsg = "";
		String CODE = code;
		String openId = (String) session.getAttribute("openId");
		
		if(StringUtils.isEmpty(openId)) {
			if(StringUtils.isEmpty(CODE)) {
				infoMsg = "无法获取微信CODE，请从微信访问页面！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			WXUser wxUser = UserInfoUtil.getWXUser(CODE);
			if("0".equals(wxUser.getSubscribe())) {
				infoMsg = "您还没有关注公众号，请先关注！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			if(StringUtils.isEmpty(wxUser.getOpenid())) {
				infoMsg = "无法获取微信openId！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			
			User user = userRepo.findByOpenId(wxUser.getOpenid());
			if(user == null) {
				infoMsg = "您还没有关注公众号，请先关注！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			if(!StringUtils.isEmpty(infoMsg)) return shuaSubmitInfo;
			
			session.setAttribute("openId", wxUser.getOpenid());
		}else {
			
		}
		List<SourceConfig> scs = scRepo.findBySourceType("doctor");
		//HTML source drop down list selector
		List<HTMLSelectOption> sources = new ArrayList<HTMLSelectOption>();
		 
		//List<HTMLSelectOption> parameters = new ArrayList<HTMLSelectOption>();
		//HTML source parameter map for selector option displaying, constructor : source id, parameter, parameter options
		Map<String, Map<String, List<HTMLSelectOption>>> sourceDisplayParamMap = new HashMap<String, Map<String, List<HTMLSelectOption>>>();
		//HTML source parameter label map for selector label displaying, constructor : source id, parameter label options
		Map<String, List<SourceParameter>> sourceDisplayLabelMap = new HashMap<String, List<SourceParameter>>();
		
		if(scs == null || scs.size() == 0) {
			log.warn("No any doctor's source config available !");
			infoMsg = "暂时没有任何可用刷源！";
			model.addAttribute("infoMsg", infoMsg);
			if(!StringUtils.isEmpty(infoMsg)) return shuaSubmitInfo;
		}else {
			for(int i=0;i<scs.size();i++) {
				HTMLSelectOption source = new HTMLSelectOption();
				SourceConfig sc = scs.get(i);
				source.setTitle(sc.getSourceKey());
				source.setValue(sc.getId());
				sources.add(source);
				
				Set<String> sourceDisplayLabelSet = new HashSet<String>();
				List<SourceParameter> sourceDisplayLabelList = new ArrayList<SourceParameter>();
				
				Map<String, List<HTMLSelectOption>> sourceDisplayParamMap2 = new HashMap<String, List<HTMLSelectOption>>();
				
				
				List<SourceParameter> spList = scs.get(i).getSourceParameter();
				
				for(int j=0;j<spList.size();j++) {
					SourceParameter sp = spList.get(j);
					if("Y".equalsIgnoreCase(sp.getDisplay())){	
						
						HTMLSelectOption displayParam = new HTMLSelectOption();
						displayParam.setTitle(sp.getName());
						displayParam.setValue(sp.getValue());
						
						
						if(sourceDisplayLabelSet.add(sp.getParameter())) {
							sourceDisplayLabelList.add(sp);
							
							if(!"?".equals(sp.getValue())) {
								List<HTMLSelectOption> sourceDisplayParamList = new ArrayList<HTMLSelectOption>();
								sourceDisplayParamList.add(displayParam);
								sourceDisplayParamMap2.put(sp.getParameter(), sourceDisplayParamList);
							}
						}else {
							if(!"?".equals(sp.getValue())) {
								sourceDisplayParamMap2.get(sp.getParameter()).add(displayParam);
							}
						}
					}
					
				}
				
				//Add blank select option at the bottom for mobile display
				for(String key : sourceDisplayParamMap2.keySet()) {
					List<HTMLSelectOption> options = sourceDisplayParamMap2.get(key);
					HTMLSelectOption displayParam = new HTMLSelectOption();
					displayParam.setTitle("");
					displayParam.setValue("");
					options.add(displayParam);
				}
				
				sourceDisplayLabelMap.put(scs.get(i).getId(), sourceDisplayLabelList);
				sourceDisplayParamMap.put(scs.get(i).getId(), sourceDisplayParamMap2);
				
			}
		}
		
		//Add blank select option at the bottom for mobile display
		HTMLSelectOption source = new HTMLSelectOption();
		source.setTitle("");
		source.setValue("");
		sources.add(source);
		
		model.addAttribute("sources", sources);
		model.addAttribute("sourceDisplayParamMap", sourceDisplayParamMap);
		model.addAttribute("sourceDisplayLabelMap", sourceDisplayLabelMap);
		
		String avoidDoubbleSubmitFlag = String.valueOf(Math.random());
		model.addAttribute("avoidDoubbleSubmitFlag", avoidDoubbleSubmitFlag);
		session.setAttribute("avoidDoubbleSubmitFlag", avoidDoubbleSubmitFlag);
		
		return "index";  
   }
	
	@RequestMapping("/doctor")  
	void weixinRedirect(@RequestParam("TARGET") String TARGET, HttpServletResponse response, Model model, HttpSession session) throws IOException {

		String openId = (String) session.getAttribute("openId");
		//String REDIRECT_URI = "http://iamz.free.ngrok.cc/doctor/home";
		String REDIRECT_URI = urlMapping.get(TARGET);
		if(StringUtils.isEmpty(openId)) {
	        String SCOPE = "snsapi_userinfo";
	        response.sendRedirect(UserInfoUtil.getCode(Constant.appId, REDIRECT_URI, SCOPE));
		}else {
			response.sendRedirect(REDIRECT_URI + "?code=fakecode&state=STAT");
		}
	}
	
	@RequestMapping("/doctor/myShuaShua")  
	String myShuaShua(HttpServletResponse response, Model model, HttpSession session,
			@RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state) throws IOException {

		String infoMsg = "";
		String CODE = code;
		String openId = (String) session.getAttribute("openId");
		
		if(StringUtils.isEmpty(openId)) {
			if(StringUtils.isEmpty(CODE)) {
				infoMsg = "无法获取微信CODE，请从微信访问页面！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			WXUser wxUser = UserInfoUtil.getWXUser(CODE);
			if("0".equals(wxUser.getSubscribe())) {
				infoMsg = "您还没有关注公众号，请先关注！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			if(StringUtils.isEmpty(wxUser.getOpenid())) {
				infoMsg = "无法获取微信openId！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			
			User user = userRepo.findByOpenId(wxUser.getOpenid());
			if(user == null) {
				infoMsg = "您还没有关注公众号，请先关注！";
				model.addAttribute("infoMsg", infoMsg);
			}
			
			if(!StringUtils.isEmpty(infoMsg)) return shuaSubmitInfo;
			
			session.setAttribute("openId", wxUser.getOpenid());
		}else {
			
		}
		
		List<Task> tasking = taskRepo.findByOpenIdAndStatusBetweenOrderByCreatedTimeDesc(openId, -0.9, 2.1);
		List<Task> taskSuccess = taskRepo.findByOpenIdAndStatusBetweenOrderByCreatedTimeDesc(openId, 2.9, 4.1);
		List<Task> taskComplete = taskRepo.findByOpenIdAndStatusBetweenOrderByCreatedTimeDesc(openId, 4.9, 100.1);
		
		model.addAttribute("tasking", tasking);
		model.addAttribute("taskSuccess", taskSuccess);
		model.addAttribute("taskComplete", taskComplete);
		
		return myShuaShua;
	}
	
	@RequestMapping("/doctor/newshuashua")  
    String newShuaShua(@RequestParam("sourceId") String sourceId, HttpSession session, 
    		@RequestParam("avoidDoubbleSubmitFlag") String avoidDoubbleSubmitFlag,
    		@RequestParam("parameter") List<String> parameters, 
    		@RequestParam("value") List<String> values, Model model) {
		
		Task task = new Task();
		String infoMsg = "";
		
		String avoidDoubbleSubmitFlagCache = (String)session.getAttribute("avoidDoubbleSubmitFlag");
		if(avoidDoubbleSubmitFlag.equals(avoidDoubbleSubmitFlagCache)) {
			session.removeAttribute("avoidDoubbleSubmitFlag");
		}else {
			infoMsg = "您的刷刷任务未能成功提交，原因：会话超时，请重新登录！";
		}
		
		
		if(StringUtils.isEmpty(sourceId)) {
			infoMsg = "您的刷刷任务未能成功提交，原因：你没有选择号源，请返回选择号源！";
		}
		model.addAttribute("infoMsg", infoMsg);
		if(!StringUtils.isEmpty(infoMsg)) return shuaSubmitInfo;
		
		
		try {
			SourceConfig sourceConfig = scRepo.findById(sourceId);
			
			if(sourceConfig == null) {
				infoMsg = "您的刷刷任务未能成功提交，原因：你选择号源无效，请返回选择号源！";
				model.addAttribute("infoMsg", infoMsg);
				if(!StringUtils.isEmpty(infoMsg)) return shuaSubmitInfo;
			}
			
			String openId = (String) session.getAttribute("openId");
			
			if(StringUtils.isEmpty(openId)) {
				infoMsg = "您的刷刷任务未能成功提交，原因：当前会话超时，请重新登录进入！";
				model.addAttribute("infoMsg", infoMsg);
				return shuaSubmitInfo;
			}
			
			User user = userRepo.findByOpenId(openId);
			//List<Task> tasksOnHand = taskRepo.findByWxIdAndStatus("oeM4n1D0CwGMiEbat7BhRtQ7wE8A", Constant.)
			
			if(user == null) {
				infoMsg = "您的刷刷任务未能成功提交，原因：您还没有关注公众号，请先关注！";
				model.addAttribute("infoMsg", infoMsg);
				return shuaSubmitInfo;
			}
			
			
			List<Task> taskOnHand = taskRepo.findByOpenIdAndStatusBetweenOrderByCreatedTimeDesc(openId, -0.9, 2.1);
			if(user.getMaxTask() >= taskOnHand.size()) {
				infoMsg = "您的刷刷任务未能成功提交，原因：您的在刷任务数不能多于" + user.getMaxTask() + "个，请等待在结束或手动取消！";
				model.addAttribute("infoMsg", infoMsg);
				return shuaSubmitInfo;
			}
			 
			 
			 task.setAmendedTime(new Date());
			 task.setStatus(Constant.TS_CREATED);
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
			 
			 
//			 /* TODO Convert sourceParameter to taskParameter
//			  */
//			 task.setTaskParameter(spList);
			 TaskUtil.buildTaskParameters(sourceConfig, task, parameters, values);
			 
			 task.setWxTaskMsgSuccessValue(sourceConfig.getWxMsgSuccessValue());
			 TaskUtil.buildWxTaskMsgValue(task, "SUCCESS", null);
			 
			 task.setWxTaskMsgErrorValue(sourceConfig.getWxMsgErrorValue());
			 TaskUtil.buildWxTaskMsgValue(task, "ERROR", null);
			 
			 task.setWxTaskMsgStartValue(sourceConfig.getWxMsgStartValue());
			 TaskUtil.buildWxTaskMsgValue(task, "START", null);
			 
			 
			 
			 TaskUtil.buildURLParameters(task);
			 
			 taskRepo.save(task);
			 
			 log.info("New task " + task.toString() + " is created.");
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Task is not created due to error : " + e.getMessage());
			
		}

		return "shuaSubmitSuccess";
	}
	
	@RequestMapping("/doctor/newshuashua2")  
    String newShuaShua2(@RequestBody List<SourceParameter> sps, Model model) {
		
		return "shuaSubmitSuccess";
	}
	
}
