package com.easycarpool.dao;

import javax.servlet.http.HttpServletRequest;

public interface UserRatingDao {
	
	public String addRating(String username, int rating);
	public String getRating(String username);
	public String addRatingThroughService(HttpServletRequest request);
}
