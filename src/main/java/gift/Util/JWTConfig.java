package gift.Util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JWTConfig {
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        String jwtSecret = env.getProperty("jwt.secretKey");
        int jwtExpirationMs = env.getProperty("jwt.expiredMs", Integer.class, 0);
        JWTUtil.init(jwtSecret, jwtExpirationMs);
    }
}
