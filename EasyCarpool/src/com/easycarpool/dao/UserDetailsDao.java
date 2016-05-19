package com.easycarpool.dao;

import java.util.List;

import com.easycarpool.entity.UserDetails;

public interface UserDetailsDao {


	public String insert(UserDetails user);
	public Object get(String userName);
}
