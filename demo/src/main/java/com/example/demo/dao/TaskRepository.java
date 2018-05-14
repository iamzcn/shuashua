package com.example.demo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.pojo.Task;

public interface TaskRepository extends MongoRepository<Task, String>{

	List<Task> findByStatus(double status);
	List<Task> findByStatusLessThanEqual(double status);
	List<Task> findByOpenIdAndStatusBetweenOrderByCreatedTimeDesc(String openId, double from, double to);
	Task findById(String id);
	List<Task> findByOpenId(String openId);
	List<Task> findByOpenIdAndStatusOrderByCreatedTimeDesc(String openId, String status);
}
