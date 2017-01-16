package mail;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javaConstants.Constants;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

public class SendEmail {
 private final String SMTP_HOST = "smtp.gmail.com";
 private final String FROM_ADDRESS = Constants.setFrom;
 private final String PASSWORD = Constants.setPassword;

 public void sendMail(String subject, String message,
   HashMap singleEmployee, String bccRecipients[],
   HttpServletRequest request, String UPLOAD_DIR_IMG)
   throws MessagingException {

  Properties props = new Properties();
  props.put("mail.smtp.host", SMTP_HOST);
  props.put("mail.smtp.port", "587");
  props.put("mail.smtp.auth", "true");
  props.put("mail.debug", "false");
  props.put("mail.smtp.tls.enable", "true");
  props.put("mail.smtp.starttls.enable", "true");

  Session session = Session.getInstance(props, new SocialAuth());

  Message msg = new MimeMessage(session);

  InternetAddress from = new InternetAddress(FROM_ADDRESS);
  msg.setFrom(from);

  InternetAddress[] myBccList = new InternetAddress[bccRecipients.length];
  String name = (String) singleEmployee.get("name");
  String recipients = (String) singleEmployee.get("email");
  String location = (String) singleEmployee.get("location");
  String id = (String) singleEmployee.get("id");
  Format formatter = new SimpleDateFormat("yyyy-MMM-dd ");
  String currentDate = formatter.format(new Date());

  for (int i = 0; i < bccRecipients.length; i++) {
   myBccList[i] = new InternetAddress(bccRecipients[i]);
  }
  if (recipients.indexOf(',') > 0) {
   msg.setRecipients(Message.RecipientType.TO,
     InternetAddress.parse(recipients));
   for (int i = 0; i < myBccList.length; i++) {
    msg.addRecipient(Message.RecipientType.BCC, myBccList[i]);
   }
  }

  else {
   msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
     recipients));

   for (int i = 0; i < myBccList.length; i++) {
    msg.addRecipient(Message.RecipientType.BCC, myBccList[i]);
   }
  }
  msg.setSubject(subject + ": " + name + " " + currentDate + " ("
    + location + ")");
  MimeMultipart multipart = new MimeMultipart("related");
  BodyPart messageBodyPart = new MimeBodyPart();
  messageBodyPart.setContent(message, "text/html");
  multipart.addBodyPart(messageBodyPart);

  BodyPart messageBodyPart2 = new MimeBodyPart();
  File f = new File(request.getServletContext().getRealPath("")
    + "/"+UPLOAD_DIR_IMG+"/" + id + ".jpg");
  DataSource fds = null;
  if (f.exists() && !f.isDirectory()) {
   fds = new FileDataSource(request.getServletContext()
     .getRealPath("") + "/"+UPLOAD_DIR_IMG+"/"+ id + ".jpg");
  } else {
   fds = new FileDataSource(request.getServletContext()
     .getRealPath("") +"/"+UPLOAD_DIR_IMG+"/0000.jpg");
  }

  messageBodyPart2.setDataHandler(new DataHandler(fds));
  messageBodyPart2.setHeader("Content-ID", "<image>");

  // add image to the multipart
  multipart.addBodyPart(messageBodyPart2);
  msg.setContent(multipart);
  // msg.setContent(message, "text/html");
  Transport.send(msg);

  System.out.println("Email sent to " + recipients + " successfully!");

 }

 class SocialAuth extends Authenticator {

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
   return new PasswordAuthentication(FROM_ADDRESS, PASSWORD);

  }
 }

}