package com.example.demo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.pojo.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByOpenId(String openId);
}
