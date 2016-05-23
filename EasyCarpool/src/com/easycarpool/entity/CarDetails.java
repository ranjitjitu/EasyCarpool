package com.easycarpool.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CarDetails implements Serializable{
	
	private static final long serialVersionUID = -7454606719051070745L;
	private String username;
	private String carMake;
	private String carVariant;
	private String registrationNumber;
	private String carColor;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCarMake() {
		return carMake;
	}
	public void setCarMake(String carMake) {
		this.carMake = carMake;
	}
	public String getCarVariant() {
		return carVariant;
	}
	public void setCarVariant(String carVariant) {
		this.carVariant = carVariant;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	
	

}
