
package com.easycarpool.dao.impl;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONObject;







import com.easycarpool.dao.UserDetailsDao;
import com.easycarpool.entity.UserDetails;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.redis.RedisWrapper;
import com.easycarpool.util.ConfigUtils;
import com.easycarpool.util.EasyCarpoolConstants;
import com.easycarpool.util.RedisCommonUtil;
import com.google.gson.Gson;

/**
 * @author ranjit_behura
 *
 */
public class UserDetailsDaoImpl implements UserDetailsDao{

	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = UserDetailsDaoImpl.class.getName();
	private static final String mapName = "ec_userDetails";
	private static final String emailMap="ec_emailDetails";
	private static RedisWrapper redisWrapper = new RedisWrapper();
	private static RedisCommonUtil commonUtil = new RedisCommonUtil();

	@Override
	public String insert(HttpServletRequest request) {
		UserDetails user = null;
		try {
			user = new UserDetails();
			String emailId=request.getParameter("email");
			if(!commonUtil.containEmailKey(emailId)){
				return "User already registered";
			}
			String userName=request.getParameter("username");
			user.setUsername(userName);
			user.setCompany(request.getParameter("company"));
			user.setEmail(emailId);
			user.setGender(request.getParameter("gender"));
			user.setPassword(request.getParameter("password"));
			user.setAge(Integer.parseInt(request.getParameter("age")));
			user.setPhoneNumber(request.getParameter("phoneNumber"));
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			String userImgName=request.getParameter("userImgName");
			InputStream is=request.getInputStream();
			byte[] data = null;
			if(is !=null){
				data=IOUtils.toByteArray(is);
				String userImgUrl=uploadUserImage(data,userImgName,userName);
				user.setUserImgUrl(userImgUrl);
			}
			redisWrapper.insert(user.getUsername(), mapName, user);
			commonUtil.insertEmail(emailId, userName);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value for userDetails for username : "+user.getUsername()+" and Exception is : "+e.getMessage());
			return "User details not saved. Try again";
		}
		return "User details saved successfully";
	}

	@Override
	public String get(HttpServletRequest request) {
		UserDetails user = null;
		String username = null;
		try {
			username = request.getParameter("username");
			user = (UserDetails)redisWrapper.get(username, mapName);
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "get", "Exception thrown while fetching values from userDetails for username :"+username+" and Exception is : "+e.getMessage());
		}
		return new Gson().toJson(user);
	}
	@Override
	public String login(HttpServletRequest request) {
		UserDetails user = null;
		JSONObject msg = new JSONObject();
		String username = null;
		try {
			username = request.getParameter("username");
			String password = request.getParameter("password");
			user = (UserDetails)redisWrapper.get(username, mapName);
			if(user !=null && user.getPassword().equals(password)){
				UUID uuid = UUID.randomUUID();
				msg.put("Status", "Success");
				msg.put("Message", "User Login Successful");
				msg.put("tokenId", uuid.toString());
				commonUtil.insertToken(username, uuid.toString());
			}else{
				msg.put("Status", "Error");
				msg.put("Message", "User Login Failed");
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "login", "Exception thrown while trying to login for username:"+username+" and Exception is : "+e.getMessage());
		}
		return msg.toString();
	}
	@Override
	public String deregisterUser(HttpServletRequest request) {
		String username = null;
		JSONObject msg = new JSONObject();
		try {
			username = request.getParameter("username");
			redisWrapper.remove(username, mapName);
			commonUtil.removeToken(username);
			msg.put("Status", "Success");
			msg.put("Message", "Deregistration Successful");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "deregisterUser", "Exception thrown while deregistering for username :"+username+" and Exception is : "+e.getMessage());
		}
		return msg.toString();
	}
	@Override
	public String logoutUser(HttpServletRequest request) {
		String username = null;
		JSONObject msg = new JSONObject();
		try {
			username = request.getParameter("username");
			commonUtil.removeToken(username);
			msg.put("Status", "Success");
			msg.put("Message", "Logout Successful");
		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "logoutUser", "Exception thrown while logout for username : "+username+" and Exception is : "+e.getMessage());
		}
		return null;
	}

	@Override
	public String updateUserDetails(HttpServletRequest request) {
		UserDetails user = null;
		try {
			user = new UserDetails();
			String userName=request.getParameter("username");
			user.setUsername(userName);
			user.setCompany(request.getParameter("company"));
			user.setEmail(request.getParameter("email"));
			user.setGender(request.getParameter("gender"));
			user.setPassword(request.getParameter("password"));
			user.setAge(Integer.parseInt(request.getParameter("age")));
			user.setPhoneNumber(request.getParameter("phoneNumber"));
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			String userImgName=request.getParameter("userImgName");
			InputStream is=request.getInputStream();
			byte[] data = null;
			if(is !=null){
				data=IOUtils.toByteArray(is);
				String userImgUrl=uploadUserImage(data,userImgName,userName);
				user.setUserImgUrl(userImgUrl);
			}
			redisWrapper.insert(user.getUsername(), mapName, user);

		} catch (Exception e) {
			logger.log(Level.ERROR, CLASS_NAME, "insert", "Exception thrown while inserting value for userDetails for username : "+user.getUsername()+" and Exception is : "+e.getMessage());
			return "User details updation failed. Try again";
		}
		return "User details updated successfully";
	}

	public String uploadUserImage(byte[] data, String fileName,String userName) throws IOException {
		String folderName=userName;
		String filePath=ConfigUtils.getProperty(EasyCarpoolConstants.ATTACHMENT_PATH)+File.separator+folderName+File.separator;
		File file1=new File(filePath);
		file1.mkdir();
		String fileFullPath=filePath+fileName;
		String name=fileName.substring(0,fileName.lastIndexOf("."));
		String ext=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		File file=new File(fileFullPath);
		int i=1;
		String newName=name;
		boolean exist=true;
		if(file.exists()){
			while(exist){
				newName=name+"_"+i;
				fileFullPath=filePath+newName+"."+ext;
				file=new File(fileFullPath);
				if(!file.exists()){
					exist=false;
					break;
				}
				i++;
			}
		}
		else{
			file.createNewFile();
		}
		FileOutputStream fOut = new FileOutputStream(fileFullPath,true);
		fOut.write(data);
		fOut.close();
		return "/"+userName+"/"+fileName;
	}
}
