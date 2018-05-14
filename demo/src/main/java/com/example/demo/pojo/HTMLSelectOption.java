package com.example.demo.pojo;

import org.springframework.stereotype.Component;

@Component
public class HTMLSelectOption {

	private String title;
	private String value;
	
	public HTMLSelectOption() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
