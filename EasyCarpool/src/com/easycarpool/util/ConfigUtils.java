package com.easycarpool.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Level;

import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;

public class ConfigUtils {

	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = ConfigUtils.class.getName();
	static Properties prop = null;
	
	static {
		InputStream in = null;
		try {
			prop = new Properties();
			String filename = "EasyCarpoolConfig.properties";
			in = ConfigUtils.class.getClassLoader().getResourceAsStream(filename);
			if(in==null){
				logger.log(Level.ERROR, CLASS_NAME, "static block","Unable to find " + filename +" file. Could not load the properties file.");
			}
			prop.load(in);
		} catch (IOException e) {
			logger.log(Level.ERROR, CLASS_NAME, "static block", "Make sure the SELFCARE_HOME environment variable is set");
			logger.log(Level.ERROR, CLASS_NAME, "static block", "Exception in PropertiesUtil constructor. message= "
					+ e.getMessage());
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}

		}
		
	}
		
	/**
	 * @param name
	 * @return String of the property value of the key "name"
	 */
	public static String getProperty(String name) {
		if (prop.containsKey(name)){
			try {
				String property = prop.getProperty(name);
				return property;
			} catch (Exception e) {
				logger.log(Level.ERROR, CLASS_NAME, "getProperty", "Exception : "+e.getMessage());
			}

		}
		return null;
	}
	
	public static void main(String[] args) {
		//TODO: Direct call
	}
	
	
	private static Properties properties;

	/**
	 * @param key
	 * @return value
	 */
	public static String getPropertyByName(String key) {

		return getInstance().getProperty(key);

	}

	/**
	 * @return property file instance
	 */
	private static Properties getInstance() {
		if (properties == null) {
			synchronized (ConfigUtils.class) {
				if (properties == null) {
					properties = new Properties();
					try {
						properties.load(ConfigUtils.class.getClassLoader()
								.getResourceAsStream(
										"eventHandling.properties"));

					} catch (FileNotFoundException e) {

						logger.log(Level.ERROR, CLASS_NAME, "getInstance", "FileNotFoundException :" + e.getMessage());
					} catch (IOException e) {

						logger.log(Level.ERROR, CLASS_NAME, "getInstance", "IOException : " + e.getMessage());
					}

				}

			}

		}

		return properties;

	}
	
	/**
	 * @param no
	 * @return
	 */
	static private byte[] toByteArr(String no) {
		byte[] number = null;
		try{
		int length = no.length();
		if (length % 2 == 1) {
			no = "0" + no;
			length++;
		}
		number = new byte[length / 2];
		int i;
		for (i = 0; i < no.length(); i += 2) {
			int j = Integer.parseInt(no.substring(i, i + 2), 16);
			number[i / 2] = (byte) (j & 0x000000ff);
		}}catch(Exception e){
			logger.log(Level.ERROR, CLASS_NAME, "toByteArr", "Exception : " + e.getMessage());
		}
		return number;
	}
}
