package com.example.demo.pojo;

public class SourceParameter {

	public SourceParameter() {}
	
	public SourceParameter(String description,String name,String parameter,String value,String display, String dataType, String callBack) {
		this.description = description;
		this.name = name;
		this.parameter = parameter;
		this.value = value;
		this.display = display;
		this.dataType = dataType;
		this.callBack = callBack;
	}
	
	private String description;
	private String name;
	private String parameter;
	private String value;
	private String display;
	private String dataType;
	private String callBack;
	
	
	
	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public String toString() {
		return "SourceParameter [description=" + description + ", name=" + name + ", parameter=" + parameter
				+ ", value=" + value + ", display=" + display + ", dataType=" + dataType + "]";
	}
	
	
}
