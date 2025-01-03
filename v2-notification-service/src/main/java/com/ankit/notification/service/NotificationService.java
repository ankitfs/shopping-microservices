package com.ankit.notification.service;

import com.ankit.order.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){
        log.info("Got message from order-placed topic {}", orderPlacedEvent);
        //send email to the customer
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springdummy@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Order with OrderNumber %s is placed successfully",
                    orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    Hi,
                    
                    Your Order with order number %s is now placed successfully.
                    
                    Best Regards,
                    Spring Dummy
                    """,
                    orderPlacedEvent.getOrderNumber()));
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Order Notification email sent !!!");
        }
        catch (MailException mx) {
            log.error("Exception occurred while sending mail:",mx);
            throw new RuntimeException("Exception occurred when sending from springdummy@email.com",mx);
        }
    }
}
