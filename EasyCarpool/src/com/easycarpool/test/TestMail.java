package com.easycarpool.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;

import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.dao.impl.UserDetailsDaoImpl;
import com.easycarpool.entity.UserDetails;
import com.easycarpool.redis.RedisWrapper;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;

public class TestMail {
	private static String userName = "easycarpooltech@gmail.com";
	private static String password = "easycarpool@123";
	public static void main(String[] args) {
//		UserDetails u = new UserDetails();
//		RedisWrapper udd = new RedisWrapper();
//		u.setUsername("ranjit_behura");
//		u.setAge(27);
//		u.setCompany("EdgeVerve");
//		u.setEmail("ranjitjitu@gmail.com");
//		u.setGender("male");
//		System.out.println(udd.insert(u));
//		UserDetails user =(UserDetails) udd.get(u.getUsername());
//		System.out.println(user.getEmail());
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");					
		String dateInString = "Wednesday, Jun 1, 2016 19:03:56 PM";	
		Date date = null;
		try {
			date = formatter.parse(dateInString);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date currDate = new Date();
		System.out.println(date.after(currDate));
	}
}
