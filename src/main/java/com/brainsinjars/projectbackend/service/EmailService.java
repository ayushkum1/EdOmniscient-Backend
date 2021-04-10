package com.brainsinjars.projectbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: Nilesh Pandit
 * @Since: 25-03-2021
 */

@Service
public class EmailService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String baseLink = "http://localhost:3000/reset_password?token=";
    private final JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String[] to, String subject, String text, Boolean isHtml) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, isHtml);

        sender.send(message);
    }

    public String getLink(String token) {
        return baseLink.concat(token);
    }

    public String getEmailBody(String token) {
        String link = getLink(token);

        StringBuilder builder = new StringBuilder();

        builder.append("<html><body><h2>Reset Password</h2><a href=\"")
                .append(link)
                .append("\">Click here to reset your password</a><br /><br /><p>Regard,</p><p>Team Brains In JARS</p></body></html>");

        return builder.toString();
    }

    public String getResetSuccessEmailBody(String randomPassword) {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><body><h2>Reset Password Success</h2><h4>Your password was reset successfully</h4><p>This is your new temporary password:<strong>")
                .append(randomPassword)
                .append("</strong></p><p>Please change your password as soon as you login.</p><br /><p>Regard,</p><p>Team Brains In JARS</p></body></html>");
        return builder.toString();
    }
}
