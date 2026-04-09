package com.tourbookingsystem.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

abstract class User {
    protected String username;
    protected String password;

    public User(String var1, String var2) {
        this.username = var1;
        this.password = normalizeStoredPassword(var2);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = hashPassword(newPassword);
    }

    public boolean checkPassword(String var1) {
        return verifyPassword(var1, this.password);
    }

    private static String normalizeStoredPassword(String storedValue) {
        if (storedValue == null || storedValue.isEmpty()) {
            return "";
        }

        if (storedValue.startsWith("sha256$")) {
            return storedValue;
        }

        return hashPassword(storedValue);
    }

    private static String hashPassword(String plainPassword) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = sha256(salt, plainPassword);
        return "sha256$" + toHex(salt) + "$" + toHex(hash);
    }

    private static boolean verifyPassword(String plainPassword, String storedValue) {
        if (storedValue == null || storedValue.isEmpty()) {
            return false;
        }

        if (!storedValue.startsWith("sha256$")) {
            return storedValue.equals(plainPassword);
        }

        String[] parts = storedValue.split("\\$", 3);
        if (parts.length != 3) {
            return false;
        }

        byte[] salt = fromHex(parts[1]);
        byte[] expectedHash = fromHex(parts[2]);
        byte[] actualHash = sha256(salt, plainPassword);

        return MessageDigest.isEqual(expectedHash, actualHash);
    }

    private static byte[] sha256(byte[] salt, String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            digest.update(plainPassword.getBytes(StandardCharsets.UTF_8));
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }

        return data;
    }
}