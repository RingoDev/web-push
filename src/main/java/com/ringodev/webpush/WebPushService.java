package com.ringodev.webpush;



import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.concurrent.ExecutionException;

@Service
public class WebPushService {


    /** The Time to live of GCM notifications */
    private static final int TTL = 255;

    public void sendPushMessage(Subscription sub, byte[] payload) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {

        // Figure out if we should use GCM for this notification somehow
        boolean useGcm = false;
        Notification notification;
        PushService pushService;

        if (!useGcm) {
            // Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
            notification = new Notification(
                    sub.getEndpoint(),
                    sub.getUserPublicKey(),
                    sub.getAuthAsBytes(),
                    payload
            );

            // Instantiate the push service, no need to use an API key for Push API
            pushService = new PushService().setKeyPair(getKeys());

        } else {
            // Or create a GcmNotification, in case of Google Cloud Messaging
            notification = new Notification(
                    sub.getEndpoint(),
                    sub.getUserPublicKey(),
                    sub.getAuthAsBytes(),
                    payload,
                    TTL
            );

            // Instantiate the push service with a GCM API key
            pushService = new PushService("gcm-api-key");
        }

        // Send the notification
        pushService.send(notification);
    }

    private KeyPair getKeys() throws IOException {

        try (InputStreamReader inputStreamReader = new InputStreamReader(new DataInputStream(new FileInputStream(new File("/home/vapid/vapid_private.pem"))))) {
            PEMParser pemParser = new PEMParser(inputStreamReader);
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
            return new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        } catch (IOException e) {
            throw new IOException("The private key could not be decrypted", e);
        }
    }
}
