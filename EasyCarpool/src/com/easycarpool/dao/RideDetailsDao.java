package com.easycarpool.dao;

import javax.servlet.http.HttpServletRequest;

public interface RideDetailsDao {
	
	public String insertRide(HttpServletRequest request);
	public String updateRideByOwner(HttpServletRequest request);
	public String confirmRideByUser(HttpServletRequest request);
	public String removeRide(HttpServletRequest request);
	public String rejectRideByOwner(HttpServletRequest request);
	public String fetchFilteredRides(HttpServletRequest request);
}
