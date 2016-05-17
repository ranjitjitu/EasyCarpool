package com.easycarpool.service;

import java.io.IOException;
import java.net.MalformedURLException;

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
	
	@RequestMapping(value= "registration", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String registrationService(HttpServletRequest request) throws MalformedURLException, IOException {
		JSONObject msg = new JSONObject();
		try{
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		return mailServer.sendEmail(email, "New Subject", "sample test mail");
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

}
