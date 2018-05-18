package com.example.demo.weixin.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.dao.TaskRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.Task;
import com.example.demo.pojo.TaskCacheHashMap;
import com.example.demo.util.TaskUtil;
import com.example.demo.weixin.service.shuashua.AsyncTaskService;

import net.sf.json.JSONObject;

@Component
public class ScheduledShuashuaTask {
	
	@Autowired
    private TaskRepository repo;
	
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	private final Logger log = LoggerFactory.getLogger(ScheduledShuashuaTask.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer count0 = 1;
//
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() throws InterruptedException {
//        System.out.println(String.format("---第%s次执行，当前时间为：%s", count0++, dateFormat.format(new Date())));
//    }
    
//    @Scheduled(fixedRate = 1000)
//	  public void reportCurrentTime() throws InterruptedException {
//    	//@SuppressWarnings("unchecked")
//		HashMap<String, Task> taskCache = TaskCacheHashMap.getInstance().getTaskMap();
//	    log.info("taskCache size = " + taskCache.size());
//	  }
//	
	private boolean isStop = false;
	JSONObject jsonObject = null; 
	
	@Scheduled(fixedRate = 5000)
	public void checkNumberTask() throws Exception{
		
		List<Task> createdTasks = repo.findByStatus(Constant.TS_CREATED);
		
		//if(createdTasks.size() == 0) log.info("No new created task.");
			
		for(int i=0; i<createdTasks.size();i++) {
			Task task = createdTasks.get(i);
			log.info("New task is detected " + task.toString() );
			task.setStatus(Constant.TS_THREADED);
			repo.save(task);
			testVoid(task, repo);
		}
		
		
		
	}
	
	
	// 测试无返回结果
    private void testVoid(Task task, TaskRepository repo) throws InterruptedException, ExecutionException{
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
//        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        // 创建了20个线程
//        for (int i = 1; i <= 20; i++) {
//            asyncTaskService.executeAsyncTask(i);
//        }

//        context.close();
    	log.info(task + " is threaded now.");
    	try {
    		asyncTaskService.executeAsyncTask(task, repo);
    	}catch(TaskRejectedException e) {
    		log.warn("Thread pool is full, wait for 1 second.");
    		Thread.sleep(1000);
    	}
    }

    // 测试有返回结果
    private void testReturn(Task task) throws InterruptedException, ExecutionException, TimeoutException {
        

        List<Future<Task>> lstFuture = new ArrayList<Future<Task>>();// 存放所有的线程，用于获取结果

        
        // 线程池超过最大线程数时，会抛出TaskRejectedException，则等待1s，直到不抛出异常为止
        Future<Task> future = asyncTaskService.asyncInvokeReturnFuture(task);
        lstFuture.add(future);
        

        // 获取值。get是阻塞式，等待当前线程完成才返回值
        for (Future<Task> f : lstFuture) {
            Task t = f.get(5000, TimeUnit.MILLISECONDS);
            repo.save(t);
        }

        
    }

	

}
