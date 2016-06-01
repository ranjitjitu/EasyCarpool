package com.easycarpool.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.easycarpool.dao.impl.UserDetailsDaoImpl;
import com.easycarpool.log.EasyCarpoolLogger;
import com.easycarpool.log.IEasyCarpoolLogger;
import com.easycarpool.mail.IMailServer;
import com.easycarpool.mail.MailServerImpl;
import com.easycarpool.util.RedisCommonUtil;

@Service(value="easyCarpoolService")
@RequestMapping("/easyCarpoolService")
public class EasyCarpoolService {
	
	private static IEasyCarpoolLogger logger = EasyCarpoolLogger.getLogger();
	private static String CLASS_NAME = EasyCarpoolService.class.getName();
	private static IMailServer mailServer = new MailServerImpl();
	private static int minRangeOTP = 000000;
	private static int maxRangeOTP = 999999;
	private static ServiceRedirectImpl serviceRedirectImpl = new ServiceRedirectImpl();
	private static RedisCommonUtil commonUtil = new RedisCommonUtil();
	
	@RequestMapping(value= "registration", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String registrationService(HttpServletRequest request) throws MalformedURLException, IOException {
		JSONObject msg = new JSONObject();
		try{
		String username = request.getParameter("username");
		if(commonUtil.containsTokenKey(username)){
			msg.put("Status", "Error");
			msg.put("Message", "Duplicate username. Please try another username");
			return msg.toString();
		}
		String email = request.getParameter("email");
		String newOTP = String.valueOf(getRandomNumberInRange(minRangeOTP,maxRangeOTP));
		String response = mailServer.sendEmail(email, "EasyCarpool Verification", newOTP , username);
		commonUtil.insertOtp(username, newOTP);
		serviceRedirectImpl.redirectService("insertUserDetails", request);
		return response;
		}catch(JSONException je){
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Mail Not Sent.Try Again,");
			} catch (JSONException e) {
				logger.log(Level.ERROR, CLASS_NAME, "registrationService", "Exception thrown in registrationService : "+e.getMessage());
			}
			
		}
		return msg.toString();
	}
	@RequestMapping(value= "verification", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String VerificationService(HttpServletRequest request) throws MalformedURLException, IOException {
		JSONObject msg = new JSONObject();
		try{
		String username = request.getParameter("username");
		String tokenId = request.getParameter("otp");
		if(commonUtil.containsOTPKey(username)){
			if(tokenId.equals(commonUtil.getOtp(username))){
				UUID uuid = UUID.randomUUID();
				msg.put("Status", "Success");
				msg.put("Message", "User Verification Successful. Enjoy");
				msg.put("tokenId", uuid.toString());
				commonUtil.insertToken(username, uuid.toString());
				commonUtil.removeOtp(username);
				return msg.toString();
			}
		}
		msg.put("Status", "Error");
		msg.put("Message", "Wrong OTP entered");
		}catch(Exception je){
			try {
				msg.put("Status", "Error");
				msg.put("Message", "User Verification Failed. Sorry");
			} catch (JSONException e) {
				logger.log(Level.ERROR, CLASS_NAME, "verificationService", "Exception thrown in verificationService : "+e.getMessage());
			}
			
		}
		return msg.toString();
	}
	private static int getRandomNumberInRange(int min, int max) {
		return (int)(Math.random() * ((max - min) + 1)) + min;
	}
	@RequestMapping(value= "defaultService/{serviceName}", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String defaultService(@PathVariable String serviceName, HttpServletRequest request){
		JSONObject msg = new JSONObject();
		try{
		String username = request.getParameter("username");
		String tokenId = request.getParameter("tokenId");
		if(username != null && tokenId != null && tokenId.equals(commonUtil.getToken(username))){
			return serviceRedirectImpl.redirectService(serviceName, request);
		}else{
			msg.put("Status", "Error");
			msg.put("Message", "Authentication Failed. Please login again and try");
		}
		}catch(Exception je){
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Service Error. Please try again");
			} catch (JSONException e) {
				logger.log(Level.ERROR, CLASS_NAME, "defaultService", "Exception thrown in defaultService : "+e.getMessage());
			}
			
		}
		return msg.toString();
	}
	@RequestMapping(value= "login", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String loginService(HttpServletRequest request) throws MalformedURLException, IOException {
		try{
		return serviceRedirectImpl.redirectService("loginUser", request);
		}catch(Exception e){	
			logger.log(Level.ERROR, CLASS_NAME, "loginService", "Exception thrown in loginService : "+e.getMessage());
		}
		return null;
	}
	

}
