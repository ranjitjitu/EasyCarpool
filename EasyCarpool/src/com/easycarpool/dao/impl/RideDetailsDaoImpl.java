package com.easycarpool.dao.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;

import com.easycarpool.dao.RideDetailsDao;
import com.easycarpool.entity.RideDetails;
import com.easycarpool.entity.UserDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;

public class RideDetailsDaoImpl implements RideDetailsDao{
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RideDetailsDaoImpl.class.getName();
	private static final String mapName = "ec_rideDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();

	@Override
	public String insertRide(HttpServletRequest request) {
		RideDetails ride = null;
		try {
			ride = new RideDetails();
			ride.setOwnerId(request.getParameter("ownerId"));
			ride.setRideId(request.getParameter("ownerId"));
			ride.setStartPoint(request.getParameter("startPoint"));
			ride.setEndPoint(request.getParameter("endPoint"));
			ride.setPitStops(request.getParameter("pitStops"));
			ride.setStartTime(request.getParameter("startTime"));
			ride.setAvailableSlots(Integer.parseInt(request.getParameter("availableSlots")));
			redisWrapper.insert(ride.getRideId(), mapName, ride);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertRide", "Exception thrown while inserting value for rideDetails for ride id : "+ride.getRideId()+" and Exception is : "+e.getMessage());
			return "Ride details saved successfully";
		}
		return "Ride details not saved. Try again";
	}

	@Override
	public String updateRideByOwner(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateRideByUser(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeRide(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
