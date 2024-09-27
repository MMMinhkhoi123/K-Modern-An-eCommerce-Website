package com.leventsclone.leventsclone.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.util.Calendar;

@Service
public class EmailSer {
    private final TemplateEngine templateEngine;
   private final JavaMailSender mailSender1;

   private final UserSer userSer;

    public EmailSer(TemplateEngine templateEngine, JavaMailSender mailSender1, UserSer userSer) {
        this.templateEngine = templateEngine;
        this.mailSender1 = mailSender1;
        this.userSer = userSer;
    }

    public String getCode() {
        int length = 20;

        // Chuỗi ký tự có thể chọn
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Tạo một đối tượng SecureRandom
        SecureRandom random = new SecureRandom();

        // StringBuilder để xây dựng chuỗi ngẫu nhiên
        StringBuilder randomString = new StringBuilder();

        // Tạo chuỗi ngẫu nhiên
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length()); // Chọn một vị trí ngẫu nhiên trong chuỗi ký tự
            char randomChar = characters.charAt(randomIndex); // Lấy ký tự ở vị trí đã chọn
            randomString.append(randomChar); // Thêm ký tự vào chuỗi ngẫu nhiên
        }
        if(userSer.getByKey(randomString.toString()).isPresent()) {
            return getCode();
        } else {
            return  randomString.toString();
        }
    }
    public String SendEmailVerify(String to) {
        String host = System.getenv("URL");
        String codeSet = getCode();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,2);
        Long time = calendar.getTimeInMillis();
        Context ctx = new Context();
        String url = host +  "/account/reset/" + time + "?verify=" + codeSet;
        ctx.setVariable("url", url);
        MimeMessage mess = mailSender1.createMimeMessage();
        try {
            MimeMessageHelper Helper = new MimeMessageHelper(mess, false, "UTF-8");
            Helper.setTo(to);
            Helper.setSubject("RESET PASSWORD YOUR ACCOUNT");
            Helper.setText("RESET PASSWORD YOUR ACCOUNT", false);
            String htmlContent = templateEngine.process("ResetPasswork", ctx);
            mess.setContent(htmlContent, "text/html; charset=utf-8");
            Helper.setText(htmlContent, true);
            mailSender1.send(mess);
            userSer.updateVerifyEmail(to, codeSet, time);
            return "success";
        } catch (MessagingException e) {
            return "fail";
        }
    }

    public String SendEmailNotifyOrder(String to, String content, String type) {
        String codeSet = getCode();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,2);
        Long time = calendar.getTimeInMillis();
        Context ctx = new Context();
        ctx.setVariable("content", content);
        ctx.setVariable("type", type);
        MimeMessage mess = mailSender1.createMimeMessage();
        try {
            MimeMessageHelper Helper = new MimeMessageHelper(mess, false, "UTF-8");
            Helper.setTo(to);
            Helper.setSubject("YOUR ORDER");
            Helper.setText("YOUR ORDER", false);
            String htmlContent = templateEngine.process("StateOrder", ctx);
            mess.setContent(htmlContent, "text/html; charset=utf-8");
            Helper.setText(htmlContent, true);
            mailSender1.send(mess);
            userSer.updateVerifyEmail(to, codeSet, time);
            return "success";
        } catch (MessagingException e) {
            return "fail";
        }
    }
}
