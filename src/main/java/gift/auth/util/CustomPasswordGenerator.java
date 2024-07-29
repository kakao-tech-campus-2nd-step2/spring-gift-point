package gift.auth.util;

import java.security.SecureRandom;
import java.util.Base64;

public class CustomPasswordGenerator {

    public static String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return Base64.getEncoder()
                .encodeToString(bytes);
    }

}
