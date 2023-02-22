package com.automationanywhere.bot.emailNotification;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

public class EmailNotification {

    private final String userName = "**";

    private final String passWord = "**";

    private final String host = "**";

        public void sendEmail (String recipient, String subject, String body) throws MessagingException {
        // Create a properties object to store SMTP information
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.starttls.enabl", "false");

        try {
            // Create a session with the SMTP server
            Session session = Session.getInstance(props, new javax.mail.Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                   return new PasswordAuthentication(userName, passWord);

                }
            });

            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            System.out.println("sending...");

            Transport transport = session.getTransport();
            // Send the message
            transport.connect(host, userName, passWord);
            transport.send(message, message.getAllRecipients());

            System.out.println("Email sent to " + recipient);
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }


}
