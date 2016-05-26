package com.easycarpool.notification;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;

import com.easycarpool.entity.DeviceInfo;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.google.gson.Gson;

public class RegisterDevice {
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RegisterDevice.class.getName();
	private static final String mapName = "ec_deviceDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();
	
	public String registerDeviceInfo(HttpServletRequest request){
		DeviceInfo deviceInfo = new DeviceInfo();
		try {
			deviceInfo.setUsername(request.getParameter("username"));
			deviceInfo.setDeviceToken(request.getParameter("deviceToken"));
			deviceInfo.setAppId(UUID.fromString(request.getParameter("appId")));
			deviceInfo.setDeviceType(request.getParameter("deviceType"));
			redisWrapper.insert(deviceInfo.getUsername(), mapName, deviceInfo);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "registerDeviceInfo", "Exception thrown while inserting value for deviceDetails for username : "+deviceInfo.getUsername()+" and Exception is : "+e.getMessage());
			return "App could not be registered";
		}
		return "App registed successfully";
	}
	public DeviceInfo getDeviceInfo(String username){
		DeviceInfo deviceInfo = new DeviceInfo();
		try {
			deviceInfo =(DeviceInfo) redisWrapper.get(username, mapName);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getDeviceInfo", "Exception thrown while fetching deviceDetails for username : "+username+" and Exception is : "+e.getMessage());
		}
		return deviceInfo;
	}
	public String removeDeviceInfo(HttpServletRequest request){
		String username = null;
		String msg = null;
		try {
			username = request.getParameter("username");
			long result = redisWrapper.remove(username, mapName);
			if(result!=0){
				msg = "Device Info removed for username : "+username;
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getDeviceInfo", "Exception thrown while fetching deviceDetails for username : "+username+" and Exception is : "+e.getMessage());
			msg = "Device Info could not be removed for username : "+username;
		}
		return msg;
	}

}
