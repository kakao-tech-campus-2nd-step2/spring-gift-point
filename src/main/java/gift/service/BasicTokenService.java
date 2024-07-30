package gift.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BasicTokenService {
    public BasicTokenService() {
    }


    public String makeTokenFrom(String userIdStr) {

        String tokenValue = "Basic " + Base64.getEncoder().encodeToString(userIdStr.getBytes());

        return tokenValue;
    }

    public Long getUserIdByDecodeTokenValue(String tokenValue) {
        String[] splitTokenValue = tokenValue.split(" ");
        String token = splitTokenValue[1];
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String userId = new String(decodedBytes);
        return Long.parseLong(userId);
    }

}
