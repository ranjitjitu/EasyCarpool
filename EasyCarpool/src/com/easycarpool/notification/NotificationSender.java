package com.easycarpool.notification;

import java.io.IOException;

import org.apache.log4j.Level;

import com.easycarpool.dao.impl.CarDetailsDaoImpl;
import com.easycarpool.entity.DeviceInfo;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class NotificationSender {
	private static String GOOGLE_SERVER_KEY;
	private static String MESSAGE_KEY;
	private static String iOSCertificateFile;
	private static String iOSPassphrase;
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = NotificationSender.class.getName();
	private static RegisterDevice registerDevice = new RegisterDevice();
	
	static{
		GOOGLE_SERVER_KEY = ConfigUtils.getProperty(EasyCarpoolConstants.GOOGLE_SERVER_KEY);
		MESSAGE_KEY = ConfigUtils.getProperty(EasyCarpoolConstants.GOOGLE_MESSAGE_KEY);
		iOSCertificateFile = ConfigUtils.getProperty(EasyCarpoolConstants.IOS_CERTIFICATE_FILE);
		iOSPassphrase = ConfigUtils.getProperty(EasyCarpoolConstants.IOS_PASSPHRASE);
	}

	public void sendNotification(String userMessage, String username){	
		Result result = null;
		try {
			DeviceInfo deviceInfo = registerDevice.getDeviceInfo(username);
			String regId  = deviceInfo.getDeviceToken();
			if(deviceInfo.getDeviceType().equalsIgnoreCase("android")){
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				Message message = new Message.Builder().timeToLive(30)
						.delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
				System.out.println("regId: " + regId);
				try{
					result = sender.send(message, regId, 1);
					logger.log(Level.INFO, CLASS_NAME, "sendNotification", "Notification Pushed to android device of "+username+" and pushStatus is : "+result.toString());
				}catch(Exception e){
					logger.log(Level.ERROR, CLASS_NAME, "sendNotification", "Exception thrown while pushing notification to android device of "+username+" and Exception : "+e);
				}
				
			}else{
				ApnsService service = APNS.newService().withCert(iOSCertificateFile, iOSPassphrase).withProductionDestination().build();
				String payload = APNS.newPayload().alertBody(userMessage).badge(1).sound("default").build();
					try {
						service.push(regId, payload);
						logger.log(Level.INFO, CLASS_NAME, "sendNotification", "Notification Pushed to iOS device of "+username+" and pushStatus is : success");
					} catch (Exception e) {
						logger.log(Level.ERROR, CLASS_NAME, "sendNotification", "Exception thrown while pushing notification to iOS device of "+username+" and Exception : "+e);
					}
			
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "sendNotification", "Exception thrown while pushing notification to "+username+" and Exception : "+e);
		}
	}
}
