package com.example.backend_spring.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * VAPT Compliance: Upgraded from AES-ECB to AES-GCM-NoPadding.
 * AES-GCM provides both confidentiality and authenticity.
 * A unique 12-byte IV is generated for every encryption and prepended to the ciphertext.
 */
@Converter
@Component
public class PiiEncryptor implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    
    private static String encryptionKey;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.security.pii-encryption-key}")
    public void setEncryptionKey(String key) {
        PiiEncryptor.encryptionKey = key;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) return null;
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] cipherText = cipher.doFinal(attribute.getBytes());

            // Prepend IV to ciphertext
            ByteBuffer bb = ByteBuffer.allocate(iv.length + cipherText.length);
            bb.put(iv);
            bb.put(cipherText);
            
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) {
            throw new RuntimeException("VAPT Error: Failed to encrypt PII data safely", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        try {
            byte[] decoded = Base64.getDecoder().decode(dbData);
            
            // Extract IV from the first 12 bytes
            ByteBuffer bb = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_LENGTH_BYTE];
            bb.get(iv);
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            // Log but don't leak details
            throw new RuntimeException("VAPT Error: Failed to decrypt PII data safely", e);
        }
    }
}
