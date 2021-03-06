package com.easycarpool.dao.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.RideDetailsDao;
import com.easycarpool.dao.UserRatingDao;
import com.easycarpool.dao.UserRidesDao;
import com.easycarpool.entity.RideDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;
import com.google.gson.Gson;

public class RideDetailsDaoImpl implements RideDetailsDao{
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = RideDetailsDaoImpl.class.getName();
	private static final String mapName = "ec_rideDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();
	private static UserRidesDao userRide = new UserRidesDaoImpl();
	private static UserRatingDao userRating = new UserRatingImpl();
	private static long timeDifferenceAllowed = Long.parseLong(ConfigUtils.getProperty(EasyCarpoolConstants.RIDE_TIME_DIFF_ALLOWED));

	@Override
	public String insertRide(HttpServletRequest request) {
		RideDetails ride = null;
		String city = null;
		String company = null;
		try {
			ride = new RideDetails();
			
			ride.setOwnerId(request.getParameter("ownerId"));
			ride.setRideId(request.getParameter("rideId"));
			ride.setStartPoint(request.getParameter("startPoint"));
			ride.setEndPoint(request.getParameter("endPoint"));
			ride.setPitStops(request.getParameter("pitStops"));
			ride.setStartTime(convertToDate(request.getParameter("startTime")));
			ride.setAvailableSlots(Integer.parseInt(request.getParameter("availableSlots")));
			city = request.getParameter("city");
			company = request.getParameter("company");
			redisWrapper.insert(ride.getRideId(), mapName+"_"+city+"_"+company, ride);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insertRide", "Exception thrown while inserting value for rideDetails for ride id : "+ride.getRideId()+" and Exception is : "+e.getMessage());
			return "Ride details not saved. Try again";
		}
		return "Ride details saved successfully";
	}

	@Override
	public String updateRideByOwner(HttpServletRequest request) {
		RideDetails ride = null;
		String city = null;
		String company = null;
		try {
			ride = new RideDetails();
			ride.setOwnerId(request.getParameter("ownerId"));
			ride.setRideId(request.getParameter("rideId"));
			ride.setStartPoint(request.getParameter("startPoint"));
			ride.setEndPoint(request.getParameter("endPoint"));
			ride.setPitStops(request.getParameter("pitStops"));
			ride.setStartTime(convertToDate(request.getParameter("startTime")));
			ride.setAvailableSlots(Integer.parseInt(request.getParameter("availableSlots")));
			city = request.getParameter("city");
			company = request.getParameter("company");
			redisWrapper.insert(ride.getRideId(), mapName+"_"+city+"_"+company, ride);

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
		String city = null;
		String company = null;
		try {
			rideId = request.getParameter("rideId");
			commuterId = request.getParameter("commuterId");
			city = request.getParameter("city");
			company = request.getParameter("company");
			ride = (RideDetails)redisWrapper.get(rideId, mapName+"_"+city+"_"+company);
			int availableSlots = ride.getAvailableSlots();
			if(availableSlots > 0){
				availableSlots = availableSlots -1;;
			}
			ride.setAvailableSlots(availableSlots);
			redisWrapper.insert(rideId, mapName+"_"+city+"_"+company, ride);
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
		String city = null;
		String company = null;
		try {
			rideId = request.getParameter("rideId");
			city = request.getParameter("city");
			company = request.getParameter("company");
			String ownerId = request.getParameter("ownerId");
			List<String> userList = userRide.getUserRideList(rideId);
			for(String commuterId : userList){
				userRide.removeUserRideEntry(commuterId, rideId);
			}
			redisWrapper.remove(rideId, mapName+"_"+city+"_"+company);
			userRating.addRating(ownerId, 0);
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
		String city = null;
		String company = null;
		try {
			rideId = request.getParameter("rideId");
			commuterId = request.getParameter("commuterId");
			city = request.getParameter("city");
			company = request.getParameter("company");
			ride = (RideDetails)redisWrapper.get(rideId, mapName+"_"+city+"_"+company);
			int availableSlots = ride.getAvailableSlots();
			availableSlots = availableSlots + 1;
			ride.setAvailableSlots(availableSlots);
			redisWrapper.insert(rideId, mapName+"_"+city+"_"+company, ride);
			userRide.removeUserRideEntry(commuterId, rideId);
			msg.put("Status", "Success");
			msg.put("Message", "Carpool Request from "+commuterId+" Rejected");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "rejectRideByOwner", "Exception thrown while rejecting ride for username : "+commuterId+" in ride id : "+rideId+" and Exception is : "+e.getMessage());
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Carpool Request from "+commuterId+" could not be Rejected. Try again");
			} catch (JSONException e1) {
				logger.log(Level.ERROR, CLASS_NAME, "rejectRideByOwner", "Exception thrown while creating json packet in rejectRideByOwner method and Exception is : "+e.getMessage());
			}			
		}
		return msg.toString();
	}
	@Override
	public String fetchFilteredRides(HttpServletRequest request) {
		String userStartPoint = null;
		String userEndPoint = null;
		String city = null;
		String company = null;
		List<RideDetails> filteredList = new ArrayList<RideDetails>();
		try {
			userStartPoint = request.getParameter("startPoint");
			userEndPoint = request.getParameter("endPoint");
			city = request.getParameter("city");
			company = request.getParameter("company");
			List<Object> rideList = redisWrapper.getAll(mapName+"_"+city+"_"+company);
			for(Object rideObj : rideList){
				RideDetails ride = (RideDetails)rideObj;
				if((ride.getStartPoint().contains(userStartPoint) || userStartPoint.contains(ride.getStartPoint()) || ride.getPitStops().contains(userStartPoint)) && (ride.getEndPoint().contains(userEndPoint) || userEndPoint.contains(ride.getEndPoint()) || ride.getPitStops().contains(userEndPoint))){
					Date rideTime = ride.getStartTime();
					Date currTime = new Date();
					if(rideTime.after(currTime)){
						long timeDiff = Math.abs(rideTime.getTime() - currTime.getTime());
						if(timeDiff/(60*1000)>=timeDifferenceAllowed){
							filteredList.add(ride);
						}
					}	
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "fetchFilteredRides", "Exception thrown while fetching rides for start point : "+userStartPoint+" and end point : "+userEndPoint+" from City : "+city+" and Exception is : "+e);			
		}
		return new Gson().toJson(filteredList);
	}

	@Override
	public String rejectRideByCommuter(HttpServletRequest request) {
		String rideId = null;
		String commuterId = null;
		JSONObject msg = new JSONObject();
		RideDetails ride = null;
		String city = null;
		String company = null;
		try {
			rideId = request.getParameter("rideId");
			commuterId = request.getParameter("commuterId");
			city = request.getParameter("city");
			company = request.getParameter("company");
			ride = (RideDetails)redisWrapper.get(rideId, mapName+"_"+city+"_"+company);
			int availableSlots = ride.getAvailableSlots();
			availableSlots = availableSlots + 1;
			ride.setAvailableSlots(availableSlots);
			redisWrapper.insert(rideId, mapName+"_"+city+"_"+company, ride);
			userRide.removeUserRideEntry(commuterId, rideId);
			userRating.addRating(commuterId, 0);
			msg.put("Status", "Success");
			msg.put("Message", "Carpool Request from "+commuterId+" Rejected");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "rejectRideByCommuter", "Exception thrown while removing requested carpool ride for username : "+commuterId+" in ride id : "+rideId+" and Exception is : "+e.getMessage());
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Carpool Request from "+commuterId+" could not be Rejected. Try again");
			} catch (JSONException e1) {
				logger.log(Level.ERROR, CLASS_NAME, "rejectRideByCommuter", "Exception thrown while creating json packet in rejectRideByCommuter method and Exception is : "+e.getMessage());
			}			
		}
		return msg.toString();
	}
	private Date convertToDate(String dateInString){
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");					
		Date date = null;
		try {
			date = formatter.parse(dateInString);
			System.out.println(date);
			System.out.println(formatter.format(date));

		} catch (ParseException e) {
			logger.log(Level.ERROR, CLASS_NAME, "convertToDate", "Exception thrown while converting "+dateInString+" into date format and Exception is : "+e);
		}
		return date;
	}

}
