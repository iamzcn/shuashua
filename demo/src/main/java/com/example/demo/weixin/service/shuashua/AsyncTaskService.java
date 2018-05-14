package com.example.demo.weixin.service.shuashua;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.example.demo.dao.TaskRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.SourceParameter;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.TaskCacheHashMap;
import com.example.demo.util.TaskUtil;
import com.example.weixin.pojo.AccessToken;
import com.example.weixin.pojo.Template;
import com.example.weixin.pojo.TemplateParam;
import com.example.weixin.util.HttpPostUtil;
import com.example.weixin.util.Sendmsg;

import net.sf.json.JSONObject;

@Component
public class AsyncTaskService {

	Random random = new Random();// 默认构造方法
	
	private final Logger log = LoggerFactory.getLogger(AsyncTaskService.class);

    @Async
    // 表明是异步方法
    // 无返回值
    public void executeAsyncTask(Task task, TaskRepository repo) {
    	
		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
    	
        try {
	        
	        Integer count = 0;
	
	        String url = task.getUrl();
			
			String regEx = task.getMonRegEx();

			Pattern pattern = Pattern.compile(regEx);
			
			List<Pattern> callBackPatternList = new ArrayList<Pattern>();
			List<SourceParameter> taskSP = task.getTaskParameter();
			for(int i=0;i<taskSP.size();i++) {
				String callBack = taskSP.get(i).getCallBack();
				if(!StringUtils.isEmpty(callBack)) {
					String op = callBack.split("===")[0];
					String ex = callBack.split("===")[1];
					
					if("regEx".equalsIgnoreCase(op)) {
						ex = ex.replaceAll("\\?", taskSP.get(i).getValue());
						
						Pattern p = Pattern.compile(ex);
						callBackPatternList.add(p);
					}else {
						
					}
				}
			}
			
			
			Task taskDB;
			
			if(StringUtils.isNotEmpty(url)) {
				task.setStatus(Constant.TS_THREAD_STARTED);
				task.setAmendedTime(new Date());
				TaskUtil.save(repo, task);
				
				if(!Constant.NOTIFICATION_START.equalsIgnoreCase(task.getNotification())) {
					task.setAmendedTime(new Date());
					task.setNotification(Constant.NOTIFICATION_START);
					TaskUtil.save(repo, task);
					TaskUtil.sendWXMsg(task, "START");
				}
				
				
				while(true) {
					
					//log.info("执行异步任务：" + Thread.currentThread().getName() + " " + task);
					count++;
	
					//taskDB = repo.findById(task.getId());
					taskDB = taskCache.get(task.getId());
					if(taskDB != null) {
						if(Constant.TS_USER_CANCELLED == taskDB.getStatus()) {
							task.setAmendedTime(new Date());
							task.setStatus(Constant.TS_SHUTDOWN_USER_CANCELLED);
							task.setNotification(Constant.NOTIFICATION_ERROR);
							TaskUtil.buildWxTaskMsgValue(task, "ERROR", "您取消了本次刷刷！");
							TaskUtil.save(repo, task);
							
							TaskUtil.sendWXMsg(task, "ERROR");
							break;
						}
					}else {
						break;
					}
					
						String ret = HttpPostUtil.doHttpPostJson(url, "");
						if(StringUtils.isNotEmpty(ret)) ret.trim();
						else {
							log.warn(Thread.currentThread().getName() + " for " + task.toString() + " return empty string, shutdown this thead.");
							task.setStatus(Constant.TS_SHUTDOWN_EMPTY_RETURN);
							task.setNotification(Constant.NOTIFICATION_ERROR);
							TaskUtil.buildWxTaskMsgValue(task, "ERROR", "监刷的源没有返回值 ，请重新选择正确条件再刷吧！");
							task.setAmendedTime(new Date());
							TaskUtil.save(repo, task);
							TaskUtil.sendWXMsg(task, "ERROR");
							break;
						}
						
						log.info(Thread.currentThread().getName() + " for task [" + task.toString() + "] this return [" + ret + "].");
						//jsonObject = JSONObject.fromObject(ret);
	
						Matcher matcher = pattern.matcher(ret);
						boolean rs = matcher.find();
						
						if(rs) {
							boolean callBackFlag = true;
							for(Pattern p : callBackPatternList) {
								Matcher m = p.matcher(ret);
								boolean r = m.find();
								if(!r) {
									callBackFlag = false;
									break;
								}
							}
							
							if(!callBackFlag) continue;
							
							log.info(Thread.currentThread().getName() + " for task [" + task.toString() + "] get available number by below return [" + ret + "].");
							task.setAmendedTime(new Date());
							task.setStatus(Constant.TS_SHUTDOWN_AVAILABLE);
							task.setNotification(Constant.NOTIFICATION_SUCCESS);
							TaskUtil.save(repo, task);
						
							
							TaskUtil.sendWXMsg(task, "SUCCESS");
							
//							Thread.currentThread().interrupt();
							//log.info(Thread.currentThread().getName() + " get available number.");
							log.info(Thread.currentThread().getName() + " will be shutdown due to get available number. " + task.toString());
							task.setStatus(Constant.TS_SHUTDOWN_NOTIFIED_USER);
							task.setAmendedTime(new Date());
							TaskUtil.save(repo, task);
							break;
						}
						if(count == task.getTryCount()) {
							log.warn(Thread.currentThread().getName() + " will be shutdown due to reached max try count. " + task.toString());
							task.setStatus(Constant.TS_SHUTDOWN_MAX_TRIED);
							task.setAmendedTime(new Date());
							task.setNotification(Constant.NOTIFICATION_ERROR);
							TaskUtil.buildWxTaskMsgValue(task, "ERROR", "监刷的源已经到达最大刷新次数，请重新选择再刷吧！");
							TaskUtil.save(repo, task);
							TaskUtil.sendWXMsg(task, "ERROR");
							break;
						}
						Thread.sleep(task.getInterval());
					
					
					
				}
			}else {
				log.warn("Task's URL is empty.");
				task.setStatus(Constant.TS_SHUTDOWN_EMPTY_URL);
				task.setNotification(Constant.NOTIFICATION_ERROR);
				task.setAmendedTime(new Date());
				TaskUtil.save(repo, task);
			}
        }catch(Exception ee) {
        	ee.printStackTrace();
        	log.error(ee.getMessage());
//			Thread.currentThread().interrupt();
			log.error(Thread.currentThread().getName() + " will be shutdown due to error: " + ee + ", " +task.toString());
			task.setStatus(Constant.TS_SHUTDOWN_ERROR);
			
			task.setAmendedTime(new Date());
			task.setException(ee.getMessage());
			
			
			try {
				TaskUtil.buildWxTaskMsgValue(task, "ERROR", ee.getMessage());
				task.setAmendedTime(new Date());
				task.setNotification(Constant.NOTIFICATION_ERROR);
				TaskUtil.save(repo, task);
				TaskUtil.sendWXMsg(task, "ERROR");
				log.info("Error message is sent to user [" + task.getOpenId() + "] for " + task.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("Error message is not sent for " + task.toString());
			}
        }finally {
        	TaskUtil.removeFromTaskCache(task);
        }
    }

    /**
     * 异常调用返回Future
     * 
     * @param i
     * @return
     * @throws InterruptedException
     */
    @Async
    public Future<Task> asyncInvokeReturnFuture(Task task) throws InterruptedException {
    	
    	boolean isStop = false;
    	JSONObject jsonObject = null; 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer count = 0;
    	
        ArrayList<String> URLs = new ArrayList<String>();
		URLs.add("https://depth-driver.souche.com/v2/api/carStockApi/getLastTwoMonthsStock.json?shopCode=GZ01&carModelCode=12213-n&packageDays=3&activityCode=ee1bfe66-02fd-4d33-8631-41fe4199c32b");
		//URLs.add("https://wap.91160.com/doctor/schedule.html?doctor_id=200105558&unit_id=200011352&dep_id=200043500");
		
		String regEx = "context";
		//regEx = "y_state_desc\":\"约满\"";
		Pattern pattern = Pattern.compile(regEx);
		
		for(int j=0;j<URLs.size();j++) {
		
			while(!isStop) {
				
				log.info("执行异步任务：" + Thread.currentThread().getName() + task);
				count++;

				try {
					String ret = HttpPostUtil.doHttpPostJson(URLs.get(j), "");
					
					jsonObject = JSONObject.fromObject(ret);

					Matcher matcher = pattern.matcher(jsonObject.toString());
					boolean rs = matcher.find();
					
					if(rs) {
						isStop = true;
						log.info(jsonObject.toString());
						Template tem=new Template();  
						tem.setTemplateId(task.getWxMsgTemplateId());  
						tem.setTopColor("#00DD00");  
						tem.setToUser(task.getOpenId());  
						tem.setUrl("");  
						          
						List<TemplateParam> paras=new ArrayList<TemplateParam>();  
						paras.add(new TemplateParam("first","野马可约啦","#FF3333"));  
						paras.add(new TemplateParam("keyword1","野马可约啦","#0044BB"));  
						paras.add(new TemplateParam("remark","Remark","#AAAAAA"));  
						          
						tem.setTemplateParamList(paras);  
						String accessToken = AccessToken.getInstance().getToken();
						Sendmsg.sendTemplateMsg(accessToken,tem);
						
						Thread.currentThread().interrupt();
						//log.info(Thread.currentThread().getName() + " get available number.");
						log.warn(Thread.currentThread().getName() + " is interrpted due to get available number. " + task.toString());
						//task.setStatus("INTERRUPTED_NOTIFIED");
						break;
					}
					if(count == task.getTryCount()) {
						Thread.currentThread().interrupt();
						//System.out.println(Thread.currentThread().getName() + " interrupted.");
						log.warn(Thread.currentThread().getName() + " is interrpted due to reached max try count. " + task.toString());
						//task.setStatus("INTERRUPTED_MAX_TRYCOUNT");
						break;
					}
					Thread.sleep(3000);
				}catch(Exception e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
					log.error(Thread.currentThread().getName() + " is interrpted due to error: " + e + ", " +task.toString());
					//task.setStatus("INTERRUPTED_ERROR");
				}
				
				
			}
		}

        Future<Task> future = new AsyncResult<Task>(task);// Future接收返回值，这里是String类型，可以指明其他类型

        return future;
    }

}
