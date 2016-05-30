package com.easycarpool.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.RideDetailsDao;
import com.easycarpool.dao.UserRidesDao;
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
	private static UserRidesDao userRide = new UserRidesDaoImpl();

	@Override
	public String insertRide(HttpServletRequest request) {
		RideDetails ride = null;
		try {
			ride = new RideDetails();
			
			ride.setOwnerId(request.getParameter("ownerId"));
			ride.setRideId(request.getParameter("rideId"));
			ride.setStartPoint(request.getParameter("startPoint"));
			ride.setEndPoint(request.getParameter("endPoint"));
			ride.setPitStops(request.getParameter("pitStops"));
			ride.setStartTime(request.getParameter("startTime"));
			ride.setAvailableSlots(Integer.parseInt(request.getParameter("availableSlots")));
			redisWrapper.insert(ride.getRideId(), mapName, ride);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertRide", "Exception thrown while inserting value for rideDetails for ride id : "+ride.getRideId()+" and Exception is : "+e.getMessage());
			return "Ride details not saved. Try again";
		}
		return "Ride details saved successfully";
	}

	@Override
	public String updateRideByOwner(HttpServletRequest request) {
		RideDetails ride = null;
		try {
			ride = new RideDetails();
			ride.setOwnerId(request.getParameter("ownerId"));
			ride.setRideId(request.getParameter("rideId"));
			ride.setStartPoint(request.getParameter("startPoint"));
			ride.setEndPoint(request.getParameter("endPoint"));
			ride.setPitStops(request.getParameter("pitStops"));
			ride.setStartTime(request.getParameter("startTime"));
			ride.setAvailableSlots(Integer.parseInt(request.getParameter("availableSlots")));
			redisWrapper.insert(ride.getRideId(), mapName, ride);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "updateRideByOwner", "Exception thrown while updating values for rideDetails for ride id : "+ride.getRideId()+" and Exception is : "+e.getMessage());
			return "Ride details updation failed. Try again";
		}
		return "Ride details updated successfully";
	}

	@Override
	public String confirmRideByUser(HttpServletRequest request) {
		RideDetails ride = null;
		String rideId = null;
		String commuterId = null;
		try {
			rideId = request.getParameter("rideId");
			commuterId = request.getParameter("commuterId");
			ride = (RideDetails)redisWrapper.get(rideId, mapName);
			int availableSlots = ride.getAvailableSlots();
			if(availableSlots > 0){
				availableSlots = availableSlots -1;;
			}
			ride.setAvailableSlots(availableSlots);
			redisWrapper.insert(rideId, mapName, ride);
			userRide.insertUserRideEntry(commuterId, rideId, false);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "confirmRideByUser", "Exception thrown while confirming ride for ride id : "+ride.getRideId()+" and Exception is : "+e.getMessage());
			return "Ride couldnt be booked";
		}
		return "Ride booked successfully";
	}

	@Override
	public String removeRide(HttpServletRequest request) {
		String rideId = null;
		JSONObject msg = new JSONObject();
		try {
			rideId = request.getParameter("rideId");
			List<String> userList = userRide.getUserRideList(rideId);
			for(String commuterId : userList){
				userRide.removeUserRideEntry(commuterId, rideId);
			}
			redisWrapper.remove(rideId, mapName);
			msg.put("Status", "Success");
			msg.put("Message", "Ride Removed SucessFully");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "removeRide", "Exception thrown while removing ride for ride id : "+rideId+" and Exception is : "+e.getMessage());
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Ride could not be removed");
			} catch (JSONException e1) {
				logger.log(Level.ERROR, CLASS_NAME, "removeRide", "Exception thrown while creating json packet in removeRide method and Exception is : "+e.getMessage());
			}			
		}
		return msg.toString();
	}
	@Override
	public String rejectRideByOwner(HttpServletRequest request) {
		String rideId = null;
		String commuterId = null;
		JSONObject msg = new JSONObject();
		RideDetails ride = null;
		try {
			rideId = request.getParameter("rideId");
			commuterId = request.getParameter("commuterId");
			ride = (RideDetails)redisWrapper.get(rideId, mapName);
			int availableSlots = ride.getAvailableSlots();
			availableSlots = availableSlots + 1;
			ride.setAvailableSlots(availableSlots);
			redisWrapper.insert(rideId, mapName, ride);
			userRide.removeUserRideEntry(commuterId, rideId);
			msg.put("Status", "Success");
			msg.put("Message", "Carpool Request from "+commuterId+" Rejected");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "removeRide", "Exception thrown while removing ride for ride id : "+rideId+" and Exception is : "+e.getMessage());
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Carpool Request from "+commuterId+" could not be Rejected. Try again");
			} catch (JSONException e1) {
				logger.log(Level.ERROR, CLASS_NAME, "removeRide", "Exception thrown while creating json packet in removeRide method and Exception is : "+e.getMessage());
			}			
		}
		return msg.toString();
	}

}
