package com.easycarpool.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceInfo implements Serializable{
	
	private static final long serialVersionUID = -6253186146260738309L;
	private UUID appId;
	private String deviceToken;
	private String deviceType;
	private String username;
	public UUID getAppId() {
		return appId;
	}
	public void setAppId(UUID appId) {
		this.appId = appId;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

}
