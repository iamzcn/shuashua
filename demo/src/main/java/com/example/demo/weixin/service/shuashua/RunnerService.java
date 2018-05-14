package com.example.demo.weixin.service.shuashua;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.demo.dao.TaskRepository;
import com.example.demo.pojo.Constant;
import com.example.demo.pojo.Task;
import com.example.demo.util.TaskUtil;

@Component
public class RunnerService implements ApplicationRunner {

	private final Logger log = LoggerFactory.getLogger(RunnerService.class);
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Override
    public void run(ApplicationArguments var1) throws Exception{
     
        List<Task> tasks = taskRepo.findByStatusLessThanEqual(Constant.TS_THREAD_STARTED);
        
        if(tasks.size() == 0) log.info("There is no started thread task in DB, no need to cache.");
        else {
        	for(int i=0;i<tasks.size();i++) {
        		Task task = tasks.get(i);
        		TaskUtil.addToTaskCache(task);
        		task.setStatus(Constant.TS_CREATED);
        		log.info(task + " is re-threaded again now.");
        	}
        	taskRepo.save(tasks);
        }
        
    }
}
