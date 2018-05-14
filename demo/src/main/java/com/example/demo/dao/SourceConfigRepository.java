package com.example.demo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.pojo.SourceConfig;

public interface SourceConfigRepository extends MongoRepository<SourceConfig, String>{

	SourceConfig findById(String id);
	
	List<SourceConfig> findBySourceType(String sourceType);
}
