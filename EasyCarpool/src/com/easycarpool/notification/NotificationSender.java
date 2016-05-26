package com.easycarpool.notification;

import java.io.IOException;

import org.apache.log4j.Level;

import com.easycarpool.dao.impl.CarDetailsDaoImpl;
import com.easycarpool.entity.DeviceInfo;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class NotificationSender {
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDA5dlLInMWVsJEUTIHV0u7maB82MCsZbU";
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = NotificationSender.class.getName();
	private static RegisterDevice registerDevice = new RegisterDevice();

	public void sendNotification(String userMessage, String username){
		final String MESSAGE_KEY = "message";	
		Result result = null;
		try {
			DeviceInfo deviceInfo = registerDevice.getDeviceInfo(username);
			String regId  = deviceInfo.getDeviceToken();
			if(deviceInfo.getDeviceType().equalsIgnoreCase("android")){
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				Message message = new Message.Builder().timeToLive(30)
						.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
				System.out.println("regId: " + regId);
				result = sender.send(message, regId, 1);
			}else{
				
			}
			logger.log(Level.INFO, CLASS_NAME, "sendNotification", "Notification Pushed to "+username+" and pushStatus is : "+result.toString());
		} catch (IOException ioe) {
			logger.log(Level.ERROR, CLASS_NAME, "sendNotification", "Exception thrown while pushing notification to "+username+" and Exception : "+ioe);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "sendNotification", "Exception thrown while pushing notification to "+username+" and Exception : "+e);
		}
	}
}
