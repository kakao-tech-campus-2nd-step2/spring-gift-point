package gift.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "my_secure_secret_key";
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(String email) {
        long now = System.currentTimeMillis() / 1000L;
        long exp = now + (EXPIRATION_TIME / 1000L);

        System.out.println("Generating token. Current time: " + now + ", Expiration time: " + exp);

        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(("{\"sub\":\"" + email + "\",\"exp\":" + exp + "}").getBytes());

        String signature = hmacSha256(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    public String extractEmail(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            try {
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                Map<String, Object> payloadMap = parsePayload(payload);

                if (payloadMap != null && validateToken(token)) {
                    return (String) payloadMap.get("sub");
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to decode Base64: " + e.getMessage());
                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println("Failed to parse payload or validate token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid token format");
        }
        return null;
    }

    public boolean validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            String signature = hmacSha256(parts[0] + "." + parts[1]);
            return signature.equals(parts[2]);
        }
        return false;
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(JwtUtil.SECRET_KEY.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    private Map<String, Object> parsePayload(String payload) {
        try {
            String[] entries = payload.replaceAll("[{}\"]", "").split(",");
            Map<String, Object> map = new HashMap<>();
            for (String entry : entries) {
                String[] keyValue = entry.split(":");
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse payload: " + e.getMessage(), e);
        }
    }

    public String getEmailFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                return extractEmail(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public boolean isTokenExpired(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            try {
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                Map<String, Object> payloadMap = parsePayload(payload);
                long exp = Long.parseLong(payloadMap.get("exp").toString());
                long now = System.currentTimeMillis() / 1000L;

                System.out.println("Token expiration time: " + exp + ", Current time: " + now);

                return exp < now;
            } catch (Exception e) {
                System.err.println("Failed to parse expiration from token: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }
}
