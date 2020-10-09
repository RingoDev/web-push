package com.ringodev.webpush;


import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Service
public class WebPushService {


    /**
     * The Time to live of GCM notifications
     */
    private static final int TTL = 255;

    private final Logger logger = LoggerFactory.getLogger(WebPushService.class);

    public void sendPushMessage(Subscription sub, byte[] payload) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {


        // Figure out if we should use GCM for this notification somehow
        Notification notification;
        PushService pushService;

        // Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
        notification = new Notification(
                sub.getEndpoint(),
                sub.getUserPublicKey(),
                sub.getAuthAsBytes(),
                payload
        );

        // Instantiate the push service, no need to use an API key for Push API
        pushService = new PushService();
        addKeys(pushService);


        // Send the notification
        logger.info("Sending Notification to");
        logger.info(notification.getEndpoint());
        logger.info(notification.getUserPublicKey().toString());
        logger.info(Arrays.toString(notification.getPayload()));
        pushService.send(notification);
    }

    private void addKeys(PushService pushService) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {

        Scanner scanner = new Scanner(new File("/home/vapid/keys.txt"));
        String pubKey = null;
        String privKey = null;

        if (scanner.hasNextLine()) {
            privKey = (scanner.nextLine());
        }
        if (scanner.hasNextLine()) {
            pubKey = (scanner.nextLine());
        }
        logger.info("Public Key: " + pubKey);

        pushService.setPublicKey(Utils.loadPublicKey(pubKey));
        pushService.setPrivateKey(Utils.loadPrivateKey(privKey));

    }
}
