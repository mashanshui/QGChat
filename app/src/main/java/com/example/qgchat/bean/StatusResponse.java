package com.example.qgchat.bean;

public class StatusResponse {

	private String status;
	private String code;
	private String description;
	
	
	public StatusResponse(String status, String code, String description) {
		this.status = status;
		this.code = code;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "StatusResponse{" +
				"status='" + status + '\'' +
				", code='" + code + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
