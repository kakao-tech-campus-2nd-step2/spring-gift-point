package gift.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordCrypto {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        System.out.println(rawPassword);
        System.out.println(encodedPassword);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
