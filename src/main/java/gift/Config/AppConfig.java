package gift.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {

    @Bean
    public MessageDigest passwordEncoder() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256");
    }

    // 추가적인 커스텀 메서드로 암호화를 구현할 수 있습니다.
    public String encodePassword(String password) {
        try {
            MessageDigest digest = passwordEncoder();
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
