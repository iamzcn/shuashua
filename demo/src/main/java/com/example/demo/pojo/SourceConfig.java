package com.example.demo.pojo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.weixin.pojo.TemplateParam;

@Document(collection = "sourceConfig")
public class SourceConfig {

	public SourceConfig() {}
	
	@Id
	private String id;
	
	private String sourceType;
	private String sourceKey;
	private String sourceFrom;
	private String baseURL;
	private String parameters;
	private String monRegEx;
	private String wxMsgTemplateId;
	private List<TemplateParam> wxMsgSuccessValue = new ArrayList<TemplateParam>();
	private List<TemplateParam> wxMsgStartValue = new ArrayList<TemplateParam>();
	private List<TemplateParam> wxMsgErrorValue = new ArrayList<TemplateParam>();
	private List<SourceParameter> sourceParameter = new ArrayList<SourceParameter>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getMonRegEx() {
		return monRegEx;
	}
	public void setMonRegEx(String monRegEx) {
		this.monRegEx = monRegEx;
	}
	public String getWxMsgTemplateId() {
		return wxMsgTemplateId;
	}
	public void setWxMsgTemplateId(String wxMsgTemplateId) {
		this.wxMsgTemplateId = wxMsgTemplateId;
	}
	
	
	public List<TemplateParam> getWxMsgSuccessValue() {
		return wxMsgSuccessValue;
	}
	public void setWxMsgSuccessValue(List<TemplateParam> wxMsgSuccessValue) {
		this.wxMsgSuccessValue = wxMsgSuccessValue;
	}
	public List<TemplateParam> getWxMsgStartValue() {
		return wxMsgStartValue;
	}
	public void setWxMsgStartValue(List<TemplateParam> wxMsgStartValue) {
		this.wxMsgStartValue = wxMsgStartValue;
	}
	public List<SourceParameter> getSourceParameter() {
		return sourceParameter;
	}
	public void setSourceParameter(List<SourceParameter> sourceParameter) {
		this.sourceParameter = sourceParameter;
	}
	public List<TemplateParam> getWxMsgErrorValue() {
		return wxMsgErrorValue;
	}
	public void setWxMsgErrorValue(List<TemplateParam> wxMsgErrorValue) {
		this.wxMsgErrorValue = wxMsgErrorValue;
	}
	
	
	
	
}
