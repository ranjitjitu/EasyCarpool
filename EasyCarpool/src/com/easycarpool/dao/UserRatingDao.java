package com.easycarpool.dao;

public interface UserRatingDao {
	
	public String addRating(String username, int rating);
	public String getRating(String username);

}
