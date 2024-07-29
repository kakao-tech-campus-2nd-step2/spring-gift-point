package gift.util;

import java.util.Base64;

public class PasswordUtil {

    public static String encodePassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String decodePassword(String encodedPassword) {
        return new String(Base64.getDecoder().decode(encodedPassword));
    }

    public static boolean matches(String dtoPassword, String entityPassword) {
        return decodePassword(entityPassword).equals(dtoPassword);
    }
}