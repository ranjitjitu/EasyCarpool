package com.easycarpool.dao.impl;

import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.UserRidesDao;
import com.easycarpool.entity.UserRides;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.easycarpool.util.RedisCommonUtil;

public class UserRidesDaoImpl implements UserRidesDao{
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = UserRidesDaoImpl.class.getName();
	private static final String mapName = "ec_userRides";
	private static RedisWrapper redisWrapper = new RedisWrapper();
	private static RedisCommonUtil commonUtil = new RedisCommonUtil();

	@Override
	public String insertUserRideEntry(String userId, String rideId,
			boolean confirmed) {
		JSONObject msg = new JSONObject();
		try {
			UserRides userRides = new UserRides();
			userRides.setUsername(userId);
			userRides.setRideId(rideId);
			userRides.setConfirmed(confirmed);
			long result = redisWrapper.insert(userId+"-"+rideId, mapName, userRides);
			if(result != 0){
				return "User Ride inserted successfully";
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertUserRideEntry", "Exception thrown while inserting userride for id : "+userId+"-"+rideId+" and Exception is : "+e.getMessage());
		}
		return "User Ride not inserted. Please try again";
	}

	@Override
	public String removeUserRideEntry(String userId, String rideId) {
		JSONObject msg = new JSONObject();
		try {
			long result = redisWrapper.remove(userId+"-"+rideId, mapName);
			if(result != 0){
				return "User Ride deleted successfully";
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "removeUserRideEntry", "Exception thrown while removing userride for id : "+userId+"-"+rideId+" and Exception is : "+e.getMessage());
		}
		return "User Ride couldnt be deleted";
	}

}
