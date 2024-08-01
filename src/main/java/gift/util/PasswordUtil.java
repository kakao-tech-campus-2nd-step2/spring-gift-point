package gift.util;


import java.util.Base64;

public class PasswordUtil {


    public static String hashPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());

    }
}