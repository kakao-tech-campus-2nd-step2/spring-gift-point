package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpiration() {
        return expiration;
    }
}
