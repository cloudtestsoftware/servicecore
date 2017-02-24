package cms.service.util;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public class MeetingRequestUtil
    {
    public boolean send(String uniqueId, Date reviewDateStartTime,
            String reviewLocation, int reviewDurationMnts, String from,
            String to, String title, String reviewSubject,
            String reviewDescription, String summary) throws Exception {
 
        reviewDateStartTime = new Date();       
        String fromMail = from;
        String toMail = to;
        uniqueId = "123456";
        reviewDescription = "testing only";
        reviewSubject = "testing review subject";
        title = "testing";
        summary = "testing summary";
        to="abcdef@xyz.com";
        reviewDurationMnts = 30;
        reviewLocation="test location";
        from=to;
        String meetingStartTime = getReviewTime(reviewDateStartTime,
                reviewDurationMnts, false);
        String meetingEndTime = getReviewTime(reviewDateStartTime,
                reviewDurationMnts, true);
         
        Properties prop = new Properties();
        StringBuffer sb = new StringBuffer();
        StringBuffer buffer = null;
        Session session = Session.getDefaultInstance(prop, null);
        Message message = new MimeMessage(session);
         
 
        try {
            prop.put("mail.smtp.host", "192.168.112.111");
            prop.put("mail.smtp.port", "25");
 
            // Define message
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress
                    .parse(to, false));
            /*
             * try { message .setRecipients( Message.RecipientType.CC,
             * InternetAddress
             * .parse("a111@xyz.com.com,a222@xyz.com,a333@xyz.com")); }
             * catch (Exception e) { System.out .println("No exception in
             * parsing recipients addresses"); System.out.println(">> Error: " +
             * e.getMessage()); }
             */
            message.setSubject(reviewSubject);
            message.setHeader("X-Mailer", "test-Mailer");
            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            buffer = sb
                    .append("BEGIN:VCALENDAR\n"
                            + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
                            + "VERSION:2.0\n" + "METHOD:REQUEST\n"
                            + "BEGIN:VEVENT\n"
                            + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
                            + to + "\n" + "ORGANIZER:MAILTO:" + from + "\n"
                            + "DTSTART:" + meetingStartTime + "\n" + "DTEND:"
                            + meetingEndTime + "\n" + "LOCATION:"
                            + reviewLocation + "\n" + "TRANSP:OPAQUE\n"
                            + "SEQUENCE:0\n" + "UID:" + uniqueId
                            + "@iquest.com\n" + "DTSTAMP:" + meetingEndTime
                            + "\n" + "CATEGORIES:Meeting\n" + "DESCRIPTION:"
                            + reviewDescription + ".\n\n" + "SUMMARY:"
                            + summary + "\n" + "PRIORITY:1\n"
                            + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
                            + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n"
                            + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
                            + "END:VEVENT\n" + "END:VCALENDAR");
            messageBodyPart.setFileName("TestMeeting.ics");
            messageBodyPart
                    .setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/iCalendar")));
            messageBodyPart.setHeader("Content-Class",
                    "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
             
        }
 
        catch (Exception me) {
            me.printStackTrace();
        }
        return false;
    }
         
    public String getReviewTime(Date reviewDateTime, int rDuration, boolean flag) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        c.setTime(reviewDateTime);
        if (flag == true) {
            c.add(Calendar.MINUTE, rDuration);
        }
        String hour = c.get(Calendar.HOUR_OF_DAY) < 10 ? "0"
                + c.get(Calendar.HOUR_OF_DAY) : ""
                + c.get(Calendar.HOUR_OF_DAY);
        String min = c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE)
                : "" + c.get(Calendar.MINUTE);
        String sec = c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND)
                : "" + c.get(Calendar.SECOND);
 
        String date = s.format(new Date(c.getTimeInMillis()));
        String dateTime = date + "T" + hour + min + sec;
        return dateTime;
    }   
 
    private class ByteArrayDataSource implements DataSource {
        private byte[] data; // data for mail message
 
        private String type; // content type/mime type
 
        ByteArrayDataSource(String data, String type) {
            try {
                // Assumption that the string contains only ascii
                // characters ! Else just pass in a charset into this
                // constructor and use it in getBytes()
                this.data = data.getBytes("iso-8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.type = type;
        }
 
        // DataSource interface methods
        public InputStream getInputStream() throws IOException {
            if (data == null)
                throw new IOException(
                        "no data exception in ByteArrayDataSource");
            return new ByteArrayInputStream(data);
        }
 
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("illegal operation in ByteArrayDataSource");
        }
 
        public String getContentType() {
            return type;
        }
 
        public String getName() {
            return "dummy";
        }
    }
 
    
    public void sendYahooMeetingRequest(Session session,String from,String subject,String recipient) throws Exception
    {
        //register the text/calendar mime type
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap)MimetypesFileTypeMap.getDefaultFileTypeMap();
        mimetypes.addMimeTypes("text/calendar ics ICS");
         
      //register the handling of text/calendar mime type
        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");
 
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
 
     // Create an alternative Multipart
       Multipart multipart = new MimeMultipart("alternative");
        
     //part 1, html text
       BodyPart messageBodyPart = buildHtmlTextPart();
       multipart.addBodyPart(messageBodyPart);
        
    // Add part two, the calendar
        BodyPart calendarPart = buildCalendarPart();
        multipart.addBodyPart(calendarPart);
         
      //Put the multipart in message 
        message.setContent(multipart);
      
        // send the message
        Transport transport = session.getTransport("smtp");
        //transport.connect();
         
       /* String host = "smtp.mail.yahoo.com";
        int port =465;// 25;
*/ 
        String host = "smtp.mail.yahoo.com";
       //String host = "smtp.gmail.com";
        int port = 587;//25;//465;//587;
         
        String username = "lonamike84";
        String password = "pass";//enter password
        transport.connect( host,port,username, password);
         
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
 
    }
    private BodyPart buildHtmlTextPart() throws MessagingException {
          
        MimeBodyPart descriptionPart = new MimeBodyPart();
  
        //Note: even if the content is spcified as being text/html, outlook won't read correctly tables at all
        // and only some properties from div:s. Thus, try to avoid too fancy content
       // String content = "<font size="\"2\"">simple meeting invitation</font>";
         
        String content = "simple meeting invitation";
        descriptionPart.setContent(content, "text/html; charset=utf-8");
  
        return descriptionPart;
    }
  
    //define somewhere the icalendar date format
    private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
  
    private BodyPart buildCalendarPart() throws Exception {
  
        BodyPart calendarPart = new MimeBodyPart();
  
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        /*cal.add(Calendar.DAY_OF_MONTH, 12);
        cal.add(Calendar.YEAR, 2011);
        cal.add(Calendar.MONTH, Calendar.SEPTEMBER);*/
         
        Date start = cal.getTime();
         
      /*  cal.add(Calendar.DAY_OF_MONTH, 12);
        cal.add(Calendar.HOUR_OF_DAY, 3);
        cal.add(Calendar.MONTH, Calendar.SEPTEMBER);*/
         
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
         
        Date end = cal.getTime();
  
        //check the icalendar spec in order to build a more complicated meeting request
        String calendarContent =
                "BEGIN:VCALENDAR\n" +
                        "METHOD:REQUEST\n" +
                        "PRODID: BCP - Meeting\n" +
                        "VERSION:2.0\n" +
                        "BEGIN:VEVENT\n" +
                        "DTSTAMP:" + iCalendarDateFormat.format(start) + "\n" +
                        "DTSTART:" + iCalendarDateFormat.format(start)+ "\n" +
                        "DTEND:"  + iCalendarDateFormat.format(end)+ "\n" +
                        "SUMMARY:test request\n" +
                        "UID:324\n" +
                        "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:lonamike84@gmail.com\n" +
                        "ORGANIZER:MAILTO:lonamike84@yahoo.com\n" +
                        "LOCATION:on the net\n" +
                        "DESCRIPTION:learn some stuff\n" +
                        "SEQUENCE:0\n" +
                        "PRIORITY:5\n" +
                        "CLASS:PUBLIC\n" +
                        "STATUS:CONFIRMED\n" +
                        "TRANSP:OPAQUE\n" +
                        "BEGIN:VALARM\n" +
                        "ACTION:DISPLAY\n" +
                        "DESCRIPTION:REMINDER\n" +
                        "TRIGGER;RELATED=START:-PT00H15M00S\n" +
                        "END:VALARM\n" +
                        "END:VEVENT\n" +
                        "END:VCALENDAR";
  
        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
  
        return calendarPart;
    }
}