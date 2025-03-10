package com.jawbr.dnd5e.exptracker.service;

import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class WebhookEncryptionService {

    private final AES256BinaryEncryptor encryptor;

    public WebhookEncryptionService(@Value("${jasypt.encryptor.secret.key}") String secretKey) {
        this.encryptor = new AES256BinaryEncryptor();
        this.encryptor.setPassword(secretKey);
    }

    public String encrypt(String data) {
        byte[] encryptedBytes = encryptor.encrypt(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        return new String(encryptor.decrypt(decodedBytes));
    }
}
