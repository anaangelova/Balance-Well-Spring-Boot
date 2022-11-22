package com.example.balancewellspringboot.service.interfaces;

import javax.mail.MessagingException;

public interface SubscriberService {
    void registerSubscriber(String email) throws MessagingException;
}
