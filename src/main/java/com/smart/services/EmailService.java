package com.smart.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

public boolean sendEmail( String subject,String message,String to) {
		
		boolean f = false;
		
		// variable for gmail
	    String host = "smtp.gmail.com";
	    
	    String from = "kurhadesam181@gmail.com";
	    
		// get the system properties
		Properties properties = System.getProperties();
		System.out.println(properties);

		// setting imp info to properties object

		// host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
//			properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// get session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("kurhadesam181@gmail.com", "zvfruxdlrnrsxamp");
			}

		});

		session.setDebug(true);

		// compose the message
		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			// from email
			mimeMessage.setFrom(from);
			// recipient email
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to msg
			mimeMessage.setSubject(subject);
			// adding text to msg
//			mimeMessage.setText(message);
			mimeMessage.setContent(message,"text/html");

			// send msg using Transport class
			Transport.send(mimeMessage);
			System.out.println("Sent Successfully");
			f=true;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return f;

	}
}
