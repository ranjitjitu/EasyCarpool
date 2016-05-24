package com.easycarpool.dao;

import java.util.List;

public interface UserRidesDao {
	
	public String insertUserRideEntry(String userId, String rideId, boolean confirmed);
	public String removeUserRideEntry(String userId, String rideId);
	public String updateUserRideEntry(String userId, String rideId);
	public List<String> getUserRideList(String rideId);

}
