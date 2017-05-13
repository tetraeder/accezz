package com.accezz.main.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {
	public static void sendMail(String msg) {
		final String username = "accezzserver@gmail.com";
		final String password = "accezz1234";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("gad.salner@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("gad.salner@gmail.com"));
			message.setSubject("Accezz Notification");
			message.setText("Dear Mail Crawler," + "\n\n" + msg);

			Transport.send(message);

			System.out.println("Email sent with message: " + msg);

		} catch (MessagingException e) {
			System.out.println("Email was not sent");
		}
	}
}
