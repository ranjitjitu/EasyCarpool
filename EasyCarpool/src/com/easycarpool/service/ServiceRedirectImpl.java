package com.easycarpool.service;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.dao.impl.UserDetailsDaoImpl;

public class ServiceRedirectImpl {
	
	//class initialization
	private static UserDetailsDao userdetails = new UserDetailsDaoImpl();
	
	private static final String INSERT_USER_DETAILS = "insertUserDetails";
	private static final String GET_USER_DETAILS = "getUserDetails";
	
	public String redirectService(String serviceName, HttpServletRequest request){
		switch (serviceName) {
		case INSERT_USER_DETAILS:
			return userdetails.insert(request);
		case GET_USER_DETAILS:
			return userdetails.get(request);
		default:
			return "Wrong service called";
		}
	}

}
