package com.sroka.grouptripsorganizer.email;


public interface EmailSender {

    void send(String to, String from, String subject, String text, boolean isHtmlContent);
}
