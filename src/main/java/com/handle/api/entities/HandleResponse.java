package com.handle.api.entities;

public class HandleResponse {
	private int handleCode;
	private String handleDesc;
	private String URL;
	
	public HandleResponse() {
		
	}
	
	public HandleResponse(int handleCode, String handleDesc, String URL) {
		this.handleCode = handleCode;
		this.handleDesc = handleDesc;
		this.URL = URL;
	}
	
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public int getHandleCode() {
		return handleCode;
	}
	public void setHandleCode(int handleCode) {
		this.handleCode = handleCode;
	}
	public String getHandleDesc() {
		return handleDesc;
	}
	public void setHandleDesc(String handleDesc) {
		this.handleDesc = handleDesc;
	}
}
