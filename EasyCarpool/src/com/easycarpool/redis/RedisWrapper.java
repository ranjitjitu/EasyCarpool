package com.easycarpool.redis;

import org.apache.log4j.Level;

import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;

public class RedisWrapper {
	private static RedisHelper redis = new RedisImpl();
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RedisWrapper.class.getName();
	
	public void insert(String key, String mapName ,Object obj) {
		try {
			redis.put(key, mapName, obj);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value : "+e.getMessage());
		} finally {
		}
	}

	public Object get(String key, String mapName) {
		try {
			return redis.get(mapName, key);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "get", "Exception thrown while in get method : "+e.getMessage());
		} 
		return null;
	}
	public void remove(String key, String mapName) {
		try {
			redis.remove(mapName, key);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "remove", "Exception thrown while in remove method : "+e.getMessage());
		} 
	}
	public boolean containKey(String key, String mapName) {
		try {
			return redis.containKey(mapName, key);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "containKey", "Exception thrown while in containKey method : "+e.getMessage());
		} 
		return false;
	}
}
