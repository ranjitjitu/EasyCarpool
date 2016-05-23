package com.easycarpool.service;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.CarDetailsDao;
import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.dao.impl.CarDetailsDaoImpl;
import com.easycarpool.dao.impl.UserDetailsDaoImpl;

public class ServiceRedirectImpl {
	
	//classes initialization
	private static UserDetailsDao userdetails = new UserDetailsDaoImpl();
	private static CarDetailsDao carDetails = new CarDetailsDaoImpl();
	
	//user details operations
	private static final String INSERT_USER_DETAILS = "insertUserDetails";
	private static final String GET_USER_DETAILS = "getUserDetails";
	private static final String LOGIN_USER = "loginUser";
	private static final String DEREGISTER_USER = "deregisterUser";
	private static final String LOGOUT_USER = "logoutUser";
	private static final String UPDATE_USER_DETAILS = "updateUserDetails";
	
	//car details operations
	private static final String INSERT_CAR_DETAILS = "insertCarDetails";
	private static final String GET_CAR_DETAILS = "getCarDetails";
	private static final String DEREGISTER_CAR = "deregisterCar";
	private static final String UPDATE_CAR_DETAILS = "updateCarDetails";
	
	public String redirectService(String serviceName, HttpServletRequest request){
		switch (serviceName) {
		case INSERT_USER_DETAILS:
			return userdetails.insert(request);
		case GET_USER_DETAILS:
			return userdetails.get(request);
		case LOGIN_USER:
			return userdetails.login(request);
		case DEREGISTER_USER:
			return userdetails.deregisterUser(request);
		case LOGOUT_USER:
			return userdetails.logoutUser(request);
		case UPDATE_USER_DETAILS:
			return userdetails.updateUserDetails(request);
		case INSERT_CAR_DETAILS:
			return carDetails.insert(request);
		case GET_CAR_DETAILS:
			return carDetails.get(request);
		case DEREGISTER_CAR:
			return carDetails.deregisterCar(request);
		case UPDATE_CAR_DETAILS:
			return carDetails.updateCarDetails(request);
		default:
			return "Wrong service called";
		}
	}

}
