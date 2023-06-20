package com.example.demo.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String urlClient;

    @Autowired
    public EmailService(JavaMailSender mailSender, @Value("${email.sender}") String from, @Value("${url.client}") String urlClient) {
        this.mailSender = mailSender;
        this.from = from;
        this.urlClient = urlClient;
    }

    public void sendEmail(String to, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");

        String htmlBody = "<html>" +
                "<body>" +
                "<h1>Contenu HTML de"+ subject + "l'e-mail</h1>" +
                "<p>Ceci est un exemple de corps HTML d'e-mail.</p>" +
                "</body>" +
                "</html>";

        message.setFrom(from);
        message.setReplyTo(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(htmlBody, true);
        message.setSentDate(new Date());

        mailSender.send(mimeMessage);
    }

    public void sendResetPasswordEmail(String to, String subject, String resetParamsLink) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");

        String resetLink = urlClient + "/reset-password"+ resetParamsLink;

        String htmlBody = "<html>" +
                "<body>" +
                "<h1>Contenu HTML de " + subject + " de l'e-mail</h1>" +
                "<a href='" + resetLink + "'>lien pour réinitialiser votre mot de passe</a>" +
                "</body>" +
                "</html>";

        message.setFrom(from);
        message.setReplyTo(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(htmlBody, true);
        message.setSentDate(new Date());

        mailSender.send(mimeMessage);
    }
}

