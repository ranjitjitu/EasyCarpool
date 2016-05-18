package com.easycarpool.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

public class MailServerImpl implements IMailServer{

	private static Properties props;
	private String userName = "easycarpooltech@gmail.com";
	private String password = "easycarpool@123";

	static{
		props = new Properties();
//		props.setProperty("proxySet","true");
//		props.setProperty("socksProxyHost","192.168.155.1");
//		props.setProperty("socksProxyPort","1080");
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
	}

	@Override
	public String sendEmail(String toAddress, String subject, String content) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject msg = new JSONObject();
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName,password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText("Dear User," +
					"\n\n Your Verification OTP is below"+
					"\n\n"+content);
			message.setSentDate(new Date());
			Transport.send(message);
			msg.put("Status", "Success");
			msg.put("Message", "Mail Sent Successfully");
			System.out.println("Done");
		}catch (MessagingException e) {
			msg.put("Status", "Error");
			msg.put("Message", "Mail Not Sent.Try Again");
			throw new RuntimeException(e);
		}catch (JSONException e) {
			msg.put("Status", "Error");
			msg.put("Message", "Mail Not Sent.Try Again.");
			throw new RuntimeException(e);
		}
		return msg.toString();

	}
}
