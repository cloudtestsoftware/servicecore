package cms.service.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TestEmailAmazonSend {
	
    public static void main(String[] args) throws Exception{
       new TestEmailAmazonSend().test();
    }

    public void test() throws Exception{
    	
    	//For north virginia
    	//final String username_n_virginia = "AKIAJGRQ5GEWKSQWIXOQ";
		//final String password_n_virginia = "AlV91FKPngN0to3s/BvwoCprrqs7zO/tm7A8zdZEzVWE";
    	//final string smtp="email-smtp.us-east-1.amazonaws.com";
 
    	
    	//for oragon
    	final String username="AKIAJKIITXVYB7TTZSGQ";
    	final String password="Aqbd3CKaZH/1tcGY2kVzqtXMNhVCNrL1v3RmBD7LRDbW";
		Properties props = new Properties();
		props.put("mail.smtp.host", "email-smtp.us-west-2.amazonaws.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("jana.srimanta@softleanerp.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("jana.srimanta@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
        
    }
}
