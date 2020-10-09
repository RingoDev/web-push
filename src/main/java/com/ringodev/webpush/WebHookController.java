package com.ringodev.webpush;

import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@RestController
public class WebHookController {

    private final Logger logger = LoggerFactory.getLogger(WebHookController.class);

private final WebPushService service;
    private final SubscriptionRepository repository;

    @Autowired
    WebHookController(SubscriptionRepository repository, WebPushService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("/github/hugo")
    public ResponseEntity<Object> githubHook(HttpServletRequest request) {
        logger.info(request.toString());
        repository.findAll().forEach((sub) -> {
            try {
                service.sendPushMessage(sub,new byte[0]);
            } catch (GeneralSecurityException | ExecutionException | IOException | JoseException | InterruptedException e) {
                logger.warn(e.toString());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<Object> testHook(HttpServletRequest request) {
        repository.findAll().forEach((sub) -> {
            try {
                HttpResponse response = service.sendPushMessage(sub,"{\"data\":\"Test succesfull\"}".getBytes(StandardCharsets.UTF_8));
                logger.info(response.toString());
                logger.info(response.getStatusLine().toString());
            } catch (GeneralSecurityException | ExecutionException | IOException | JoseException | InterruptedException e) {
                logger.warn(e.toString());
            }
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
