package com.easycarpool.dao;

import javax.servlet.http.HttpServletRequest;

public interface CarDetailsDao {
	
	public String insert(HttpServletRequest request);
	public String get(HttpServletRequest request);
	public String deregisterCar(HttpServletRequest request);
	public String updateCarDetails(HttpServletRequest request);
	
}
