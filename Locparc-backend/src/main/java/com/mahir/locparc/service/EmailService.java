package com.mahir.locparc.service;

import com.mahir.locparc.dao.UserDao;
import com.mahir.locparc.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    private static final String FROM_EMAIL = "locparc.noreply@gmail.com";

    private final JavaMailSender mailSender;
    private final UserDao userDao;

    public EmailService(JavaMailSender mailSender, UserDao userDao) {
        this.mailSender = mailSender;
        this.userDao = userDao;
    }

    public void sendEmail(
            String destinationEmail,
            String subject,
            String body
    ) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(FROM_EMAIL);
        mailMessage.setTo(destinationEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        mailSender.send(mailMessage);
    }


    public void sendEmailToAdmins(
            String subject,
            String body
    ) {
        Optional<List<User>> optionalAdmins = userDao.findAllAdmins();
        if (optionalAdmins.isPresent()) {
            List<User> admins = optionalAdmins.get();
            for (User admin: admins) {
                sendEmail(admin.getEmail(), subject, body);
            }
        }
    }

    public void sendEmailToLenders(
            String subject,
            String body
    ) {
        Optional<List<User>> optionalLenders = userDao.findAllLenders();
        if (optionalLenders.isPresent()) {
            List<User> admins = optionalLenders.get();
            for (User admin: admins) {
                sendEmail(admin.getEmail(), subject, body);
            }
        }
    }
}
