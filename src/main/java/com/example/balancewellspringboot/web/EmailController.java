package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.service.interfaces.SubscriberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;

@Controller
@RequestMapping("/sendEmail")
public class EmailController {

    private final SubscriberService subscriberService;
    public EmailController( SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public String sendEmail(@RequestParam String emailAddress) throws MessagingException {
        subscriberService.registerSubscriber(emailAddress);
        return "redirect:/home?subscribed=true";
    }

}
