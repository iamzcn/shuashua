package com.example.demo.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {

	public User() {}
	
	@Id
	private String id;
	
	private String openId;
	private int tryCount;
	private int interval;
	private String favorite;
	private int maxTask;
	
	
	
	public int getMaxTask() {
		return maxTask;
	}
	public void setMaxTask(int maxTask) {
		this.maxTask = maxTask;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getTryCount() {
		return tryCount;
	}
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", openId=" + openId + ", tryCount=" + tryCount + ", interval=" + interval + "]";
	}
	
	
}
