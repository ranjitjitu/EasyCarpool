package com.easycarpool.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easycarpool.mail.IMailServer;
import com.easycarpool.mail.MailServerImpl;

@Service(value="easyCarpoolService")
@RequestMapping("/easyCarpoolService")
public class EasyCarpoolService {
	
	private static IMailServer mailServer = new MailServerImpl();
	private static HashMap<String, String> tokenList = new HashMap<>();
	private static int minRangeOTP = 000000;
	private static int maxRangeOTP = 999999;
	
	@RequestMapping(value= "registration", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String registrationService(HttpServletRequest request) throws MalformedURLException, IOException {
		JSONObject msg = new JSONObject();
		try{
		String username = request.getParameter("username");
		if(tokenList.containsKey(username)){
			msg.put("Status", "Error");
			msg.put("Message", "Duplicate username. Please try another username");
			return msg.toString();
		}
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String newOTP = String.valueOf(getRandomNumberInRange(minRangeOTP,maxRangeOTP));
		String response = mailServer.sendEmail(email, "EasyCarpool Verification", newOTP);
		tokenList.put(username, newOTP);
		return response;
		}catch(JSONException je){
			try {
				msg.put("Status", "Error");
				msg.put("Message", "Mail Not Sent.Try Again");
			} catch (JSONException e) {
				e.printStackTrace();
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
		String tokenId = request.getParameter("tokenId");
		if(tokenList.containsKey(username)){
			if(tokenId.equals(tokenList.get(username))){
				msg.put("Status", "Success");
				msg.put("Message", "User Verification Successful!!!");
				return msg.toString();
			}
		}
		msg.put("Status", "Error");
		msg.put("Message", "Wrong OTP entered");
		}catch(Exception je){
			try {
				msg.put("Status", "Error");
				msg.put("Message", "User Verification Failed!!!");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return msg.toString();
	}
	private static int getRandomNumberInRange(int min, int max) {
		return (int)(Math.random() * ((max - min) + 1)) + min;
	}

}
