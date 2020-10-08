package com.ringodev.webpush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebHookController {

    private final Logger logger = LoggerFactory.getLogger(WebHookController.class);


    @PostMapping("/github/hugo")
    public ResponseEntity<Object> githubHook(HttpServletRequest request){
        logger.info(request.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
