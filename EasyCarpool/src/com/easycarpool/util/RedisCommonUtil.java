package com.easycarpool.util;

import org.apache.log4j.Level;

import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisHelper;
import com.easycarpool.redis.RedisImpl;

public class RedisCommonUtil {
	
	private static RedisHelper redis = new RedisImpl();
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RedisCommonUtil.class.getName();
	private String otpMapName = "globalOtpMap";
	private String tokenMapName = "tokenOtpMap";
	private String emailMapName="ec_emailMap";
	
	public void insertOtp(String key,String value){
		try {
			redis.put(key, otpMapName, value);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertOtp", "Exception thrown while inserting value in "+otpMapName+" : "+e.getMessage());
		} 
	}
	public String getOtp(String key){
		try {
			return (String)redis.get(otpMapName, key);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getOtp", "Exception thrown while fetching value from "+otpMapName+" : "+e.getMessage());
		} 
		return null;
	}
	public void removeOtp(String key){
		try {
			redis.remove(otpMapName, key);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "removeOtp", "Exception thrown while removing value from "+otpMapName+" : "+e.getMessage());
		} 
	}
	public boolean containsOTPKey(String key) {
		try {
			return redis.containKey(otpMapName, key);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "containsOTPKey", "Exception thrown while checking key from "+otpMapName+" : "+e.getMessage());
		} 
		return false;
	}
	public void insertToken(String key,String value){
		try {
			redis.put(key, tokenMapName, value);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertToken", "Exception thrown while inserting value in "+tokenMapName+" : "+e.getMessage());
		}
	}
	public String getToken(String key){
		try {
			return (String)redis.get(tokenMapName, key);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "getToken", "Exception thrown while fetching value from "+tokenMapName+" : "+e.getMessage());
		} 
		return null;
	}
	public void removeToken(String key){
		try {
			redis.remove(tokenMapName, key);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "removeToken", "Exception thrown while removing value from "+tokenMapName+" : "+e.getMessage());
		} 
	}public boolean containsTokenKey(String key) {
		try {
			return redis.containKey(tokenMapName, key);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "containsTokenKey", "Exception thrown while checking key from "+tokenMapName+" : "+e.getMessage());
		} 
		return false;
	}
	public boolean containEmailKey(String emailId){
		try {
			return redis.containKey(emailMapName, emailId);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "containEmailKey", "Exception thrown while checking key from "+emailMapName+" : "+e.getMessage());
		} 
		return false;
	}
	public void insertEmail(String emailId,String userName){
		try {
			redis.put(emailId, emailMapName, userName);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertEmail", "Exception thrown while inserting value in  "+emailMapName+" : "+e.getMessage());
		}
	}
}
