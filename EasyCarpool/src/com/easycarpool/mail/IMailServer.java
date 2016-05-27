package com.easycarpool.mail;

import org.codehaus.jettison.json.JSONException;

public interface IMailServer {
	
	public String sendEmail(String toAddress, String subject, String content, String username)  throws JSONException;

}
