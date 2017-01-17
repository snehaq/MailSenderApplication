package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javaConstants.Constants;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

public class PasswordRecoveryMail {
	String host = "smtp.gmail.com";
	String port = "587";
	String mailFrom = Constants.setFrom;
	String password = Constants.setPassword;

	public void sendHtmlEmail(HttpServletRequest request)
			throws AddressException, MessagingException, FileNotFoundException,
			IOException {

		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailFrom, password);
			}
		};

		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);
		long millisecond = new Date().getTime();
		String timestamp = String.valueOf(millisecond);
		;
		GenericUtility.changeTimeStamp(timestamp, request);

		String mailTo = request.getParameter("email");
		String subject = "Password Recovery";
		String link = request.getRequestURL().toString()
				+ "?mode=checkExpiry&token=" + millisecond;
		String message = "\n<a href="
				+ link
				+ ">Click here to reset password</a>\nThis link will expire in 24 hrs or if new request sent";
		msg.setFrom(new InternetAddress(mailFrom));
		InternetAddress[] toAddresses = { new InternetAddress(mailTo) };
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		// set plain text message
		msg.setContent(message, "text/html");

		// sends the e-mail
		Transport.send(msg);

	}
}