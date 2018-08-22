package com.example.demo.controler;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.example.demo.pojo.HTMLSelectOption;
import com.example.demo.pojo.Task;
import com.example.demo.util.TaskUtil;

@RestController
public class RESTController {
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private SourceConfigRepository scRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	private final Logger log = LoggerFactory.getLogger(RESTController.class);
	
	
//	@RequestMapping(value = "/getSelect", method = RequestMethod.POST)  
//	public Map<String, List<HTMLSelectOption>> getSelect(@RequestParam("sourceId") String sourceId, @RequestParam("position") String position) {  
//		
//		
//	}
	
	
	
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
