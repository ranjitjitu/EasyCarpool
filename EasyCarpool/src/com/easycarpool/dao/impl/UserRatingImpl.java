package com.easycarpool.dao.impl;

import org.apache.log4j.Level;

import com.easycarpool.dao.UserRatingDao;
import com.easycarpool.entity.RatingDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.google.gson.Gson;

public class UserRatingImpl implements UserRatingDao{
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = UserRatingImpl.class.getName();
	private static final String mapName = "ec_ratingDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();

	@Override
	public String addRating(String username, int rating) {
		try {
			RatingDetails ratingDetails = (RatingDetails)redisWrapper.get(username, mapName);
			if(ratingDetails == null){
				ratingDetails = new RatingDetails();
				ratingDetails.setUsername(username);
				ratingDetails.setAverageRating(rating);
				ratingDetails.setNumberOfEntries(1);
			}else{
				float avgRating = ratingDetails.getAverageRating();
				int numberOfEntries = ratingDetails.getNumberOfEntries();
				float newRating = ((avgRating*numberOfEntries)+rating)/(float)(numberOfEntries+1);
				ratingDetails.setAverageRating(newRating);
				ratingDetails.setNumberOfEntries(numberOfEntries+1);
			}
			long result = redisWrapper.insert(username, mapName, ratingDetails);
			if(result != 0){
				return "Rating for username : "+username+" updated successfully";
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "addRating", "Exception thrown while adding ratingDetails for username :"+username+" and Exception is : "+e);
		}
		return "Rating for username : "+username+" could not be updated";
	}

	@Override
	public String getRating(String username) {
		RatingDetails ratingDetails = null;
		try {
			ratingDetails = (RatingDetails)redisWrapper.get(username, mapName);
			if(ratingDetails == null){
				ratingDetails = new RatingDetails();
				ratingDetails.setUsername(username);
				ratingDetails.setAverageRating(0);
				ratingDetails.setNumberOfEntries(0);
			}
			redisWrapper.insert(username, mapName, ratingDetails);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "addRating", "Exception thrown while adding ratingDetails for username :"+username+" and Exception is : "+e);
		}
		return new Gson().toJson(ratingDetails);
	}

}
