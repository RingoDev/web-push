package com.ringodev.webpush;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class WebpushApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebpushApplication.class, args);
        Security.addProvider(new BouncyCastleProvider());
    }

}
