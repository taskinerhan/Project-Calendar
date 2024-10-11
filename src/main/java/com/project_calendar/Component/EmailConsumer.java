package com.project_calendar.Component;

import com.project_calendar.Configure.RabbitMQConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailConsumer {

    @Autowired
    private JavaMailSender emailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveEmail(String emailContent) throws MessagingException, IOException {
        String[] emailParts = emailContent.split(";", 3);
        String to = emailParts[0];
        String subject = emailParts[1];
        String content = emailParts[2];

        sendEmail(to, subject, content);
    }

    public void sendEmail(String to, String subject, String content) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Limit Notification</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">\n" +
                "<div style=\"background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: 0 auto;\">\n" +
                "    <h1 style=\"color: #b5b4fb;\">Limit Information!</h1>\n" +
                "    <p>" + content + "</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        helper.setText(htmlContent, true);
        emailSender.send(message);
    }
}
