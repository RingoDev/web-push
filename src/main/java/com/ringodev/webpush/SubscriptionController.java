package com.ringodev.webpush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("subscription")
public class SubscriptionController {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @CrossOrigin(origins="https://www.ringodev.com")
    @PostMapping("/add")
    public ResponseEntity<Object> addSubscription(@RequestBody Subscription subscription, HttpServletRequest request){
        logger.info("Tried to add Subscription");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
