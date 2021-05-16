package com.contactmanager.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

public boolean sendEmail(String subject,String msg,String to) {
		
		boolean f=false;
		
		      String from="abhijitsarkar012ssdd@gmail.com";
		
		    // gmail host
				String host = "smtp.gmail.com";

				// get the system properties
				Properties properties = System.getProperties();

				System.out.println(properties);

				// setting info to properties

				// host set
				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.auth", "true");

				// 1. to get session object

				Session session = Session.getInstance(properties, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {

						return new PasswordAuthentication("abhijitsarkar012ssdd@gmail.com", "marich@123AB");
					}
				});

				session.setDebug(true);

				// 2. compose the message
				MimeMessage m = new MimeMessage(session);

				try {

					// from email id
					m.setFrom(from);

					// adding recipient
					m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

					// adding subject to message
					m.setSubject(subject);

					// adding message
				//	m.setText(msg);
					
					m.setContent(msg,"text/html");

					// 3. send message using transport class

					Transport.send(m);

					System.out.println("sent success-----------");
					f=true;

				} catch (MessagingException e) {
					e.printStackTrace();
				}

		return f;
	}

}
