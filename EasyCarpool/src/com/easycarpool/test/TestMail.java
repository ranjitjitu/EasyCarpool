package com.easycarpool.test;

import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.dao.impl.UserDetailsDaoImpl;
import com.easycarpool.entity.UserDetails;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;

public class TestMail {
	private static String userName = "easycarpooltech@gmail.com";
	private static String password = "easycarpool@123";
	public static void main(String[] args) {
		UserDetails u = new UserDetails();
		UserDetailsDao udd = new UserDetailsDaoImpl();
		u.setUsername("ranjit_behura");
		u.setAge(27);
		u.setCompany("EdgeVerve");
		u.setEmail("ranjitjitu@gmail.com");
		u.setGender("male");
		System.out.println(udd.insert(u));
		UserDetails user =(UserDetails) udd.get(u.getUsername());
		System.out.println(user.getEmail());
	}
}
