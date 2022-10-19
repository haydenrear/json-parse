package com.hayden.jsonparsebeffe.parsing;

import com.hayden.jsonparsebeffe.model.AppEmailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

  JavaMailSender mailSender;
  @Value("${app.mail.bcc}")
  String bcc;


  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public boolean sendMessage(AppEmailMessage message){
    return sendMessage(message.getText(), message.getFrom(), message.getTo(), message.getFile());
  }

  public boolean sendMessage(String message, String from, String to, File file) {
    MimeMessage mimeMailMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setBcc(bcc);
      helper.setText(message);
      if(file != null)
        helper.addAttachment("Owner App Attachment", file);
      mailSender.send(helper.getMimeMessage());
    } catch (MessagingException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
