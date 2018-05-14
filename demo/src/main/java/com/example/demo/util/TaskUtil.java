package com.example.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dao.TaskRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.SourceConfig;
import com.example.demo.pojo.SourceParameter;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.TaskCacheHashMap;
import com.example.weixin.pojo.AccessToken;
import com.example.weixin.pojo.Template;
import com.example.weixin.pojo.TemplateParam;
import com.example.weixin.util.Sendmsg;

public class TaskUtil {

	public static final Logger log = LoggerFactory.getLogger(TaskUtil.class);
	
	public static void buildURLParameters(Task task) throws Exception {
		
		List<SourceParameter> spList = task.getTaskParameter();
		StringBuffer sb = new StringBuffer();
		sb.append(task.getBaseURL());
		if(spList.size() == 0) log.warn("No URL parameters for " + task.toString());
		else {
			for(int i=0;i<spList.size();i++) {
				SourceParameter sp = spList.get(i);
				if(i>0) sb.append("&");
				sb.append(sp.getParameter() + "=" + sp.getValue());
			}
		}
		
		task.setUrl(sb.toString());
			
	}
	
	public static void buildTaskParameters(SourceConfig sourceConfig, Task task, List<String> parameters, List<String> values) throws Exception {
		
		if(task == null || parameters == null || parameters.size() == 0 || values == null || values.size() == 0) {
			log.warn("Either task or parameters or values is empty !");
			return;
		}
		
		List<SourceParameter> spList = sourceConfig.getSourceParameter();
		List<SourceParameter> taskSPList = new ArrayList<SourceParameter>();
		List<String> taskParamCheckList = new ArrayList<String>();
		
		for(int i=0;i<spList.size();i++) {
			SourceParameter sp = spList.get(i);
			//if the config parameter is added to task parameter, then no need to add again
			if(taskParamCheckList.contains(sp.getParameter())) continue;
			
			//If parameter is displayed for user to input, find the value from parameters and values
			if("Y".equalsIgnoreCase(sp.getDisplay())) {
				String sourceParam = sp.getParameter();
				for(int j=0;j<parameters.size();j++) {
					if(taskParamCheckList.contains(sp.getParameter())) continue;
					
					String p = parameters.get(j);
					if(sourceParam.equals(p)) {
						if("?".equals(sp.getValue())) {
							sp.setValue(values.get(j));
							sp.setName(values.get(j));
							taskSPList.add(sp);
							taskParamCheckList.add(sp.getParameter());
						}else {
							//Also need to match the value, to get the correct doctor name
							for(String v : values) {
								if(sp.getValue().equals(v)) {
									sp.setValue(v);
									taskSPList.add(sp);
									taskParamCheckList.add(sp.getParameter());
									break;
								}
							}
						}
					}
				}
			}else {
				//Otherwise perform inner operator
				if(sp.getValue().startsWith("INNER_OPERATOR")) {
					String o = sp.getValue().split("-")[1];
					if("RANDOM".equals(o)) {
						Double r = Math.random();
						sp.setValue(String.valueOf(r));
						taskSPList.add(sp);
						taskParamCheckList.add(sp.getParameter());
					}
				}else {
					//Other case use task config parameter as task parameter
					taskSPList.add(sp);
					taskParamCheckList.add(sp.getParameter());
				}
				
			}
			
		}
		task.setTaskParameter(taskSPList);
	}
	
	public static void buildWxTaskMsgValue(Task task, String msgType, String customMessage) throws Exception {
		
		List<TemplateParam> taskParas = new ArrayList<TemplateParam>();  
		
		if("SUCCESS".equalsIgnoreCase(msgType))	taskParas = task.getWxTaskMsgSuccessValue();
		else if("START".equalsIgnoreCase(msgType)) taskParas = task.getWxTaskMsgStartValue();
		else if("ERROR".equalsIgnoreCase(msgType)) taskParas = task.getWxTaskMsgErrorValue();
		
		List<TemplateParam> tpList = taskParas;
		if(tpList.size() == 0) log.warn("No WX message template parameter found for " + task.toString());
		else {
			for(int i=0;i<tpList.size();i++) {
				TemplateParam tp = tpList.get(i);
				String tpValue = tp.getValue();
				if(tp != null) {
					String spph = "\\{sourceParameter.";
					if(tp.getValue() != null) {
						//tp.getValue() : {sourceParameter.doctorId.name} {sourceParameter.hospitalId.name} {sourceParameter.deptId.name} {sourceParameter.regDate.value} 可约啦
						//t1[0] : there is no place holder in this section
						//t1[1] : doctorId.name} 
						//t1[2] : hospitalId.name} 
						String[] t1 = tp.getValue().split(spph);
						//As t1[0] has no place holder so starts from 1
						for(int j=1;j<t1.length;j++) {
							
							String[] t2 = t1[j].split("\\}");
							// Only contains "}" then treat it as inner operator 
							//t2[0] : doctorId.name
							if(t2.length > 0) {
								String param = t2[0].split("\\.")[0];
								String field = t2[0].split("\\.")[1];
								
								//Find the parameter and get the corresponding field value
								for(SourceParameter sp : task.getTaskParameter()) {
									if(param.equals(sp.getParameter())) {
										switch(field) {
										case "name": tpValue = tpValue.replaceAll(spph + t2[0] + "\\}", sp.getName()); break;
										case "description": tpValue = tpValue.replaceAll(spph + t2[0] + "\\}", sp.getDescription()); break;
										case "parameter": tpValue = tpValue.replaceAll(spph + t2[0] + "\\}", sp.getParameter()); break;
										case "value": tpValue = tpValue.replaceAll(spph + t2[0] + "\\}", sp.getValue()); break;
										}
										break;
									}
								}
							}
						}
						
						if(customMessage != null) tpValue = tpValue.replaceAll("\\{customMessage\\}", customMessage);
					}
					
					String taskph = "\\{task.";
					if(tp.getValue() != null) {
						//tp.getValue() : 来自 {task.sourceKey} {sourceParameter.doctorId.name} {sourceParameter.hospitalId.name} {sourceParameter.deptId.name} {sourceParameter.regDate.value} 可约啦
						//t1[0] : there is no place holder in this section
						//t1[1] : sourceKey} XXXX 
						String[] t1 = tp.getValue().split(taskph);
						//As t1[0] has no place holder so starts from 1
						for(int j=1;j<t1.length;j++) {
							
							String[] t2 = t1[j].split("\\}");
							// Only contains "}" then treat it as inner operator 
							//t2[0] : sourceKey
							if(t2.length > 0) {
								String field = t2[0];
								
								//Find the field value of current task
								switch(field) {
								case "sourceKey": tpValue = tpValue.replaceAll(taskph + t2[0] + "\\}", task.getSourceKey()); break;
								}
							}
						}
						
						if(customMessage != null) tpValue = tpValue.replaceAll("\\{customMessage\\}", customMessage);
					}
				}
				tp.setValue(tpValue);
			}
		}
		
	}
	
	
	
	public static void sendWXMsg(Task task, String msgType) throws Exception{
		Template tem=new Template();  
		tem.setTemplateId(task.getWxMsgTemplateId());  
		tem.setTopColor("#00DD00");  
		tem.setToUser(task.getOpenId());  
		tem.setUrl("www.baidu.com");  
		
		List<TemplateParam> paras=new ArrayList<TemplateParam>();  
		List<TemplateParam> taskParas = new ArrayList<TemplateParam>();  
		
		if("SUCCESS".equalsIgnoreCase(msgType))	taskParas = task.getWxTaskMsgSuccessValue();
		else if("START".equalsIgnoreCase(msgType)) taskParas = task.getWxTaskMsgStartValue();
		else if("ERROR".equalsIgnoreCase(msgType)) taskParas = task.getWxTaskMsgErrorValue();
		
		for(int i=0;i<taskParas.size();i++) {
			TemplateParam tp = taskParas.get(i);
			if(tp != null)
				paras.add(new TemplateParam(tp.getName(), tp.getValue(), tp.getColor()));  
		}
		          
		tem.setTemplateParamList(paras);  
		String accessToken = AccessToken.getInstance().getToken();
		Sendmsg.sendTemplateMsg(accessToken,tem);
	}
	
	public static void sendWXMsg(String templateId, String wxId, List<TemplateParam> paras){
		
		Template tem=new Template();  
		tem.setTemplateId(templateId);  
		tem.setTopColor("#00DD00");  
		tem.setToUser(wxId);  
		tem.setUrl("www.baidu.com");  
		
		for(int i=0;i<paras.size();i++) {
			TemplateParam tp = paras.get(i);
			if(tp != null)
				paras.add(new TemplateParam(tp.getName(), tp.getValue(), tp.getColor()));  
		}
		          
		tem.setTemplateParamList(paras);  
		String accessToken = AccessToken.getInstance().getToken();
		Sendmsg.sendTemplateMsg(accessToken,tem);
		
	}
	
	public static void sendWXErrorMsg(String templateId, String wxId, String error){
		
		Template tem=new Template();  
		tem.setTemplateId(templateId);  
		tem.setTopColor("#00DD00");  
		tem.setToUser(wxId);  
		tem.setUrl("");  
		
		List<TemplateParam> paras=new ArrayList<TemplateParam>();  
		paras.add(new TemplateParam("first","野马可约啦","#FF3333"));  
		paras.add(new TemplateParam("keyword1","野马可约啦","#0044BB"));  
		paras.add(new TemplateParam("remark","Remark","#AAAAAA"));  
		          
		tem.setTemplateParamList(paras);  
		String accessToken = AccessToken.getInstance().getToken();
		Sendmsg.sendTemplateMsg(accessToken,tem);
		
	}
	
	public static Task save(TaskRepository repo, Task task) {
	
		
		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
		if(Constant.TS_THREAD_STARTED == task.getStatus()) {
			taskCache.put(task.getId(), task);
		}else {
			taskCache.remove(task.getId());
		}
		
		repo.save(task);
		
		return task;
	}
	
	public static void updateTaskCacheStatus(String id, double status) {
		
		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
		
		if(taskCache != null) {
			if(taskCache.containsKey(id)) {
				taskCache.get(id).setStatus(status);
			}
		}
	}
	
	public static void addToTaskCache(Task task) {
		
		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
		
		if(taskCache != null) {
			if(!taskCache.containsKey(task.getId())) {
				taskCache.put(task.getId(), task);
			}
		}
	}
	
	public static void removeFromTaskCache(Task task) {
		
		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
		
		if(taskCache != null) {
			if(taskCache.containsKey(task.getId())) {
				taskCache.remove(task.getId());
			}
		}
	}
	
}
