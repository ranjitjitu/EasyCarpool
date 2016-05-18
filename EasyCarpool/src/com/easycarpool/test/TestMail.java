package com.easycarpool.test;

import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;

public class TestMail {
	private static String userName = "easycarpooltech@gmail.com";
	private static String password = "easycarpool@123";
	public static void main(String[] args) {
		ConfigUtils conf = new ConfigUtils();
		System.out.println(conf.getProperty(EasyCarpoolConstants.DATABASE_URL));
	}
}
