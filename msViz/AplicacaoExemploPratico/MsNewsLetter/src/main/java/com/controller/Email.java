package com.controller;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.entities.MessageUser;
import com.entities.User;

public class Email {

	public static void sendEmail(User user) {

		System.out.println("fake email to " + user.getEmail());
		// Assuming you are sending email from localhost

		// Recipient's email ID needs to be mentioned.
		String recipient = user.getEmail();

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		String body = "There are a new products in ToyExample";
		MessageUser m = new MessageUser(recipient, body);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(m.getSender()));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(m.getRecipient()));

			// Set Subject: header field
			// message.setSubject("This is the Subject Line!");

			// Now set the actual message
			message.setText(m.getBody());

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public static void sendEmailToAll(List<User> allUsers) {
	}
}
