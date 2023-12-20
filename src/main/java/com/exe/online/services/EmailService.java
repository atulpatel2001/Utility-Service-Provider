package com.exe.online.services;

import com.exe.online.entity.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.util.Properties;

@Service
public class EmailService {
    private Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final String password = "dopltwynkjbqbzun";
    private final String userName = "utilityserviceprovider2023@gmail.com";

    private final String from = "utilityserviceprovider2023@gmail.com";

    public Properties getProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }

    public boolean sendEmail(Email email) {
        boolean f = false;

        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email.getTo())));
            mimeMessage.setSubject(email.getSubject());
            mimeMessage.setContent(email.getMassage(), "text/html");
//            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            logger.info("SuccessFull Send Email");
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Something Went Wrong");
        }
        return f;
    }

    public boolean sendAttachEmail(Email email, ByteArrayInputStream pdfInputStream) {
        boolean f = false;
        //step 1 get the session object
        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        session.setDebug(true);
        //step 2 compose the message[text,multi media]

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            //from
            mimeMessage.setFrom(new InternetAddress(from));
            //adding recepient
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email.getTo())));
            //adding subject
            mimeMessage.setSubject(email.getSubject());
            //send file


            MimeMultipart mimeMultipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            MimeBodyPart filePart = new MimeBodyPart();

            textPart.setText(email.getMassage(), "UTF-8", "html");

            //file

            filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(pdfInputStream, "application/pdf")));
            filePart.setFileName(email.getFile());
            // filePart.attachFile(email.getFile());

            mimeMultipart.addBodyPart(textPart);
            mimeMultipart.addBodyPart(filePart);

            mimeMessage.setContent(mimeMultipart);
            //send
            //step 3 :send message

            Transport.send(mimeMessage);
            logger.info("SuccessFull Send Email");
            f = true;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Something Went Wrong");
        }
        return f;
    }


}
