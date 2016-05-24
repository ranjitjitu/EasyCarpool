package com.easycarpool.dao;

public interface UserRidesDao {
	
	public String insertUserRideEntry(String userId, String rideId, boolean confirmed);
	public String removeUserRideEntry(String userId, String rideId);

}
