package com.easycarpool.dao;

import javax.servlet.http.HttpServletRequest;


public interface UserDetailsDao {


	public String insert(HttpServletRequest request);
	public String get(HttpServletRequest request);
	public String login(HttpServletRequest request);
	public String deregisterUser(HttpServletRequest request);
	public String logoutUser(HttpServletRequest request);
	public String updateUserDetails(HttpServletRequest request);
	
}
