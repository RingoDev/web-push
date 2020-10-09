package com.ringodev.webpush;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import javax.persistence.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Entity
class Subscription {

    public Subscription() {
        // Add BouncyCastle as an algorithm provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @JsonProperty("key")
    @Column(nullable = false)
    String sub_key;

    @JsonProperty("auth")
    @Column(nullable = false)
    String sub_auth;

    @Column(nullable = false)
    String endpoint;

    public void setSub_auth(String auth) {
        this.sub_auth = auth;
    }

    public String getSub_auth() {
        return sub_auth;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setSub_key(String key) {
        this.sub_key = key;
    }

    public String getSub_key() {
        return sub_key;
    }

    /**
     * Returns the base64 encoded auth string as a byte[]
     */
    public byte[] getAuthAsBytes() {
        return Base64.getDecoder().decode(getSub_auth());
    }


    /**
     * Returns the base64 encoded public key string as a byte[]
     */
    public byte[] getKeyAsBytes() {
        return Base64.getDecoder().decode(getSub_key());
    }

    /**
     * Returns the base64 encoded public key as a PublicKey object
     */
    public PublicKey getUserPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

        return kf.generatePublic(pubSpec);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "key='" + sub_key + '\'' +
                ", auth='" + sub_auth + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }
}
