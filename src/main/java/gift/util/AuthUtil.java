package gift.util;

import java.util.Base64;

public class AuthUtil {

    public static String[] decodeBasicAuth(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded);
            return credentials.split(":", 2);
        }
        return null;
    }
}
