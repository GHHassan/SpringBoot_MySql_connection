package com.example.sqlconnect.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Math.random;
@Component
public class PublicUserId {

    private final Random random = new SecureRandom();
    private final int ITERATION = 1000;
    private final int KEY_LENGTH = 245;

    public String generateRandomId(int length) {
        return this.generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String ALPHABET = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
            stringBuilder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return stringBuilder.toString();
    }
}
