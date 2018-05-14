package com.example.demo.pojo;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class TaskCacheHashMap {
	private static TaskCacheHashMap instance = new TaskCacheHashMap();
	
	private HashMap<String, Task> taskMap = new HashMap<String, Task>();
	
	private TaskCacheHashMap() {
		
	}
	
	public static TaskCacheHashMap getInstance() {
		return instance;
	}

	public HashMap<String, Task> getTaskMap() {
		return taskMap;
	}

	public void setTaskMap(HashMap<String, Task> taskMap) {
		this.taskMap = taskMap;
	}

	
}
