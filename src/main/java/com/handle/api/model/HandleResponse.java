package com.handle.api.model;

public class HandleResponse {
	private int handleCode;
	private String handleDesc;
	private String URL;
	private String status;
	private String statusDesc;
	
	public HandleResponse() {
		
	}
	
	public HandleResponse(int handleCode, String handleDesc, String uRL, String status, String statusDesc) {
		super();
		this.handleCode = handleCode;
		this.handleDesc = handleDesc;
		URL = uRL;
		this.status = status;
		this.statusDesc = statusDesc;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
}
