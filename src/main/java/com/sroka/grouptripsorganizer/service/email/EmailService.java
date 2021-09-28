package com.sroka.grouptripsorganizer.service.email;


import com.sroka.grouptripsorganizer.email.EmailSender;
import com.sroka.grouptripsorganizer.exception.EmailSendFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.sroka.grouptripsorganizer.configuration.mail.MailConfig.MAIL_THREAD_POOL_TASK_EXECUTOR_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements EmailSender {
    private static final String ENCODING = "UTF-8";

    private final JavaMailSender javaMailSender;

    @Override
    @Async(MAIL_THREAD_POOL_TASK_EXECUTOR_NAME)
    @Retryable(value = EmailSendFailException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delayInMilliseconds}", multiplierExpression = "${retry.delayMultiplier}", maxDelayExpression = "${retry.maxDelayInMilliseconds}"))
    public void send(String to, String from, String subject, String text, boolean isHtmlContent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, ENCODING);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, isHtmlContent);
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailSendFailException("failed to send email");
        }
    }

    @Recover
    public void recover(EmailSendFailException e, String to) {
        log.error("Could not send email to:" + to);
    }
}
