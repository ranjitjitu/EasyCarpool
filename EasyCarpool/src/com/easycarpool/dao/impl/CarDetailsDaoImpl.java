package com.easycarpool.dao.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.CarDetailsDao;
import com.easycarpool.entity.CarDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.google.gson.Gson;

public class CarDetailsDaoImpl implements CarDetailsDao {
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = CarDetailsDaoImpl.class.getName();
	private static final String mapName = "ec_carDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();

	@Override
	public String insert(HttpServletRequest request) {
		CarDetails car = null;
		try {
			car = new CarDetails();
			car.getCarColor();
			car.setUsername(request.getParameter("username"));
			car.setCarMake(request.getParameter("carmake"));
			car.setCarVariant(request.getParameter("carvariant"));
			car.setRegistrationNumber(request.getParameter("registrationnumber"));
			redisWrapper.insert(car.getUsername(), mapName, car);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value for carDetails for username : "+car.getUsername()+" and Exception is : "+e.getMessage());
			return "Car details not saved. Try again";
		}
		return "Car details saved successfully";
	}

	@Override
	public String get(HttpServletRequest request) {
		CarDetails car = null;
		String username = null;
		try {
			username = request.getParameter("username");
			car = (CarDetails)redisWrapper.get(username,mapName);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "get", "Exception thrown while fetching carDetails for username : "+car.getUsername()+" and Exception is : "+e.getMessage());
		}
		return new Gson().toJson(car);
	}

	@Override
	public String deregisterCar(HttpServletRequest request) {
		String username = null;
		JSONObject msg = new JSONObject();
		try {
			username = request.getParameter("username");
			redisWrapper.remove(username,mapName);
			msg.put("Status", "Success");
			msg.put("Message", "Car Deregistration Successful");

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "deregisterCar", "Exception thrown while removing carDetails for username : "+username+" and Exception is : "+e.getMessage());
		}
		return msg.toString();
	}

	@Override
	public String updateCarDetails(HttpServletRequest request) {
		CarDetails car = null;
		try {
			car = new CarDetails();
			car.setUsername(request.getParameter("username"));
			car.setCarMake(request.getParameter("carmake"));
			car.setCarVariant(request.getParameter("carvariant"));
			car.setRegistrationNumber(request.getParameter("registrationnumber"));
			redisWrapper.insert(car.getUsername(), mapName, car);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "updateCarDetails", "Exception thrown while inserting value for carDetails for username : "+car.getUsername()+" and Exception is : "+e.getMessage());
			return "Car details updation failed. Try again";
		}
		return "Car details updated successfully";
	}
	@Override
	public int checkIfCarRegistered(HttpServletRequest request) {
		String username = null;
		try {
			username = request.getParameter("username");
			if(redisWrapper.containKey(username,mapName)){
				return 1;
			}

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "checkIfCarRegistered", "Exception thrown while checking value for carDetails for username : "+username+" and Exception is : "+e.getMessage());
		}
		return 0;
	}

}
