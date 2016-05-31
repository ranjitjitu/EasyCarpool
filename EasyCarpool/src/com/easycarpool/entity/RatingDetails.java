package com.easycarpool.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RatingDetails implements Serializable{
	
	private static final long serialVersionUID = -6147835108920897854L;
	private String username;
	private float averageRating;
	private int numberOfEntries;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public float getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}
	

}
