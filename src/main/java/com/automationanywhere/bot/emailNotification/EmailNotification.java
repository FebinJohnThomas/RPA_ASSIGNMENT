package com.automationanywhere.bot.emailNotification;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotification {

    public static void sendEmail(String recipient, String subject, String body) throws MessagingException {
        // Create a properties object to store SMTP information
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.socketFactory.port", "465");
//      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");



        // Create a session with the SMTP server
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("febin.thomas@xcubated.com", "Employee.management@1234");
            }
        });

        // Create a message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("febin.thomas@xcubated.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport transport = session.getTransport();
        // Send the message
        transport.connect("smtp.office365.com", "febin.thomas@xcubated.com", "Employee.management@1234");
        transport.send(message, message.getAllRecipients());

//        System.out.println("Email sent to " + recipient);
    }
}
