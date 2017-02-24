// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EmailUtility.java

package cms.service.util;


import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import cms.service.app.ApplicationConstants;
import cms.service.template.TemplateTable;
import cms.service.template.TemplateUtility;



// Referenced classes of package semantic.util:
//            SendMailUsingAuthentication

public class EmailUtility
{
	static Log logger = LogFactory.getLog(EmailUtility.class);
	
	//for amazon
	static final String FROM = "jana.srimanta@gmail.com"; // Replace with your "From" address. This address must be verified.
	 static final String TO = "jana.srimanta@gmail.com"; // Replace with a "To" address. If you have not yet requested
	 // production access, this address must be verified.
	 
	 static final String BODY = "This email was sent through the Amazon SES SMTP interface by using Java.";
	 static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
	 // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
	 static final String SMTP_USERNAME = "AKIAJGRQ5GEWKSQWIXOQ"; // Replace with your SMTP username.
	 static final String SMTP_PASSWORD = "AlV91FKPngN0to3s/BvwoCprrqs7zO/tm7A8zdZEzVWE"; // Replace with your SMTP password.
	 // Amazon SES SMTP host name. This example uses the us-east-1 region.
	 static final String HOST = "email-smtp.us-east-1.amazonaws.com";
	 
	 // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
	 // STARTTLS to encrypt the connection.
	 static final int PORT = 465;
	
	//Keep email resource name in the /USER-INF/application.xml ="emailResource";
	
	private String sender;
	private String password;
	private Properties prop=new Properties();;
	private Element resourceElm;
	private TemplateUtility tu;
	private ResourceUtility resource;
	
    public EmailUtility(String resourcepath)
    {
        tu = new TemplateUtility();
        resource=new ResourceUtility(resourcepath);
        resourceElm=resource.getResourceElement(ApplicationConstants.EMAIL_RESOURCE);
    	if(resourceElm!=null){
    		if(ApplicationConstants.GENERATE_LOG){
    			logger.info("***** Using Resource Element XML="+resourceElm.asXML());
    			logger.info("***** SMTP="+resourceElm.attributeValue("smtp")+ " , port="+resourceElm.attributeValue("port"));
    		}
	        sender=resourceElm.attributeValue("sender");
	    	password=resourceElm.attributeValue("password");
			prop.put("mail.smtp.host", resourceElm.attributeValue("smtp"));
			prop.put("mail.smtp.socketFactory.port", resourceElm.attributeValue("port"));
			prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.port", resourceElm.attributeValue("port"));
    	}else{
    		 logger.info("***** Resource Element is null for element name="+ApplicationConstants.EMAIL_RESOURCE);
    	}
    }

    public EmailUtility() {
		// TODO Auto-generated constructor stub
	}

	public boolean processEmailForBidRequest(TemplateTable cdata, String parentobjid)
    {
        
        if(cdata.getRowCount() > 0)
        {
            String message2bidrequest = cdata.getFieldValue("message2bidrequest", cdata.getRowCount() - 1);
            String header = cdata.getFieldValue("messageheader", cdata.getRowCount() - 1);
            String message = cdata.getFieldValue("messagebody", cdata.getRowCount() - 1);
            String name = cdata.getFieldValue("name", cdata.getRowCount() - 1);
            String messagebody = String.valueOf(String.valueOf((new StringBuffer("\n Dear Sir,\n\n")).append(header).append("\n\n").append(message).append("\n\n Bidding Team")));
            
          
            TemplateTable bidderemail = tu.getResultSet("select email from table_bidder where email is not null and bidder2bidrequest=".concat(String.valueOf(String.valueOf(parentobjid))));
           
            for(int i = 0; i < bidderemail.getRowCount(); i++)
            {
                String sendto = bidderemail.getFieldValue("email", i);
                this.sendEmail( sendto, name, messagebody);
            }

        }
        return true;
    }

    public void sendEmailWithAttachment( String to, String subjectline, String messagebody, String filename)
    {
        try
        {
        	Session session = this.getSession(this.prop,this.sender,this.password);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.sender));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subjectline);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messagebody);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        }
        catch(MessagingException mex)
        {
            mex.printStackTrace();
            logger.info("ERROR Sending Email:"+ mex.getMessage());
           
        }
    }

    public void sendEmail( String sendto, String subjectline, String message)
    {
        try
        {
           	Session session = this.getSession(this.prop,this.sender,this.password);
            //session.setDebug(false);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            InternetAddress address[] = {
                new InternetAddress(sendto)
            };
            msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
            msg.setSubject(subjectline);
            msg.setSentDate(new Date());
            msg.setText(message);
            Transport.send(msg);
        }
        catch(MessagingException mex)
        {
            mex.printStackTrace();
            logger.info("ERROR Sending Email:"+ mex.getMessage());
        }
    }

    
    
    public long sendPasswordCode(String loginname)
    {
    	 List<String> receipent=new ArrayList<String>();
    	
    	 Random rand = new Random(); 
    	 int low = 9999;
    	 int high = 9999999;
    	 long resetcode=rand.nextInt(high - (low - 1)) + low;
    	 String sql="update table_testuser set verifypassword='"+resetcode+"' where loginname='"+loginname+"'";
         receipent.add(loginname);
         
        try
        {
            boolean pwupadte = tu.executeQuery(sql);
          
            if(pwupadte)
            {
                String msg = "\nHi, \n\t\tYour password code is= "+rand.nextInt() +"\n\t\tPlease use this code to reset your password.";
               
                this.postMail(receipent, "Your Password Reset Code", msg);
            	if(ApplicationConstants.GENERATE_LOG){
            		logger.info("Sucessfully sent password reset code to user="+loginname +" Reset Code="+resetcode);
            	}
            }else{
            	if(ApplicationConstants.GENERATE_LOG){
            		logger.info("FAILED to update password reset code to user="+loginname +" Reset Code="+resetcode);
            	}
            } 
        }
        catch(MessagingException mex)
        {
            mex.printStackTrace();
            logger.info("ERROR Sending Email:"+ mex.getMessage());
        }
        return resetcode;
       
    }

    public boolean sendMessage(String sendto,String subject,String message)
    {
        List<String> receipent=new ArrayList<String>();
        receipent.add(sendto);
        try
        {
        	this.postMail(receipent, subject, message);
            logger.info("Sucessfully Sent mail to All Users");
           return true;
        }
        catch(MessagingException mex)
        {
            mex.printStackTrace();
            logger.info("");
            return false;
        }
       
    }
    
    
    private Session getSession( final Properties prop, final String sender,final String password){
    	 Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(sender, password);
    			}
    		  });
    	 return session;
    }
    
    private void postMail( List<String> recipients , String subject,
            String message ) throws MessagingException
{
		
		
		//Session session = this.getSession(this.prop,this.sender,this.password);
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		  });
		
		
		// create a message
		Message msg = new MimeMessage(session);
		
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(sender);
		msg.setFrom(addressFrom);
		
		InternetAddress[] addressTo = new InternetAddress[recipients.size()];
		int i=0;
		for (String recipent:recipients)
		{
			addressTo[i] = new InternetAddress(recipent);
			i++;
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		
		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
		
}
  
    
    public static void main(String[] args){
    	
    	String resourcepath="";
    	EmailUtility e= new EmailUtility(resourcepath);
    	e.sendMessage("jana.srimanta@gmail.com", "Account Create","Just for testing");
    	//e.sendEmail("107.21.103.155", "info@paychecknext.com", "jana.srimanta@gmail.com","Test Message from paychecknext.com", "This is test message");
    }
}
