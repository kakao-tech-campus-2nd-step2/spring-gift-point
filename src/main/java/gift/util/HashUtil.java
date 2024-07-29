package gift.util;

import gift.exception.FailedHashException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {

    public static String hashPassword(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new FailedHashException();
        }
        return bytesToHex(digest.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
