package edu.cvtc.hooked.util;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {
    // need to create an email to send reset from
    private static final String from = "capstone-hooked@gmail.com";
    private static final String password = "CapstoneHooked!";

    public static void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Message sent successfully, please wait, and then refresh your email for the link.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }
}
