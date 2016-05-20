package com.easycarpool.dao;

import javax.servlet.http.HttpServletRequest;


public interface UserDetailsDao {


	public String insert(HttpServletRequest request);
	public String get(HttpServletRequest request);
}
