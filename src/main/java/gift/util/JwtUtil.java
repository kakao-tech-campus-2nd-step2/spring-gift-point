package gift.util;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        long exp = now + EXPIRATION_TIME;

        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(("{\"sub\":\"" + email + "\",\"exp\":" + exp + "}").getBytes());

        String signature = hmacSha256(header + "." + payload, SECRET_KEY);

        return header + "." + payload + "." + signature;
    }

    public String extractEmail(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3 && validateToken(token)) {
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return payload.split(",")[0].split(":")[1].replace("\"", "");
        }
        return null;
    }

    public boolean validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            String signature = hmacSha256(parts[0] + "." + parts[1], SECRET_KEY);
            return signature.equals(parts[2]);
        }
        return false;
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }
}
