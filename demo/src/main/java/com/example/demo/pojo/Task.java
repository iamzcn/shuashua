package com.example.demo.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.weixin.pojo.TemplateParam;

@Document(collection = "task")
public class Task {

	 @Id
	 private String id;
	 
	 private String url;
	 private double status;
	 private Date createdTime;
	 private Date amendedTime;
	 private String sourceType;
	 private String sourceKey;
	 private String sourceFrom;
	 private String baseURL;
	 private String parameters;
	 private String openId;
	 private String wxMsgTemplateId;
	 private int tryCount;
	 private int interval;
	 private String monRegEx;
	 private String exception;
	 private String notification;
	 private List<TemplateParam> wxTaskMsgSuccessValue = new ArrayList<TemplateParam>();
	 private List<TemplateParam> wxTaskMsgStartValue = new ArrayList<TemplateParam>();
	 private List<TemplateParam> wxTaskMsgErrorValue = new ArrayList<TemplateParam>();
	 private List<SourceParameter> taskParameter = new ArrayList<SourceParameter>();
	 
	 public Task() {}
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public double getStatus() {
		return status;
	}

	public void setStatus(double status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getAmendedTime() {
		return amendedTime;
	}
	public void setAmendedTime(Date amendedTime) {
		this.amendedTime = amendedTime;
	}
	
	
	

	public List<TemplateParam> getWxTaskMsgErrorValue() {
		return wxTaskMsgErrorValue;
	}

	public void setWxTaskMsgErrorValue(List<TemplateParam> wxTaskMsgErrorValue) {
		this.wxTaskMsgErrorValue = wxTaskMsgErrorValue;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public List<TemplateParam> getWxTaskMsgSuccessValue() {
		return wxTaskMsgSuccessValue;
	}

	public void setWxTaskMsgSuccessValue(List<TemplateParam> wxTaskMsgSuccessValue) {
		this.wxTaskMsgSuccessValue = wxTaskMsgSuccessValue;
	}

	public List<TemplateParam> getWxTaskMsgStartValue() {
		return wxTaskMsgStartValue;
	}

	public void setWxTaskMsgStartValue(List<TemplateParam> wxTaskMsgStartValue) {
		this.wxTaskMsgStartValue = wxTaskMsgStartValue;
	}

	public List<SourceParameter> getTaskParameter() {
		return taskParameter;
	}

	public void setTaskParameter(List<SourceParameter> taskParameter) {
		this.taskParameter = taskParameter;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getMonRegEx() {
		return monRegEx;
	}

	public void setMonRegEx(String monRegEx) {
		this.monRegEx = monRegEx;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getWxMsgTemplateId() {
		return wxMsgTemplateId;
	}

	public void setWxMsgTemplateId(String wxMsgTemplateId) {
		this.wxMsgTemplateId = wxMsgTemplateId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", sourceKey=" + sourceKey + ", sourceFrom=" + sourceFrom +  "]";
	}

	
	 
}
