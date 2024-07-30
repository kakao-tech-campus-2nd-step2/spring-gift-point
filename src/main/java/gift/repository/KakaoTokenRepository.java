package gift.repository;

import gift.constants.ErrorMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class KakaoTokenRepository {

    private static final Map<String, String> accessToken = new HashMap<>();

    public static void saveAccessToken(String email, String accessToken) {
        KakaoTokenRepository.accessToken.put(email, accessToken);
    }

    public static String getAccessToken(String email) {
        if (!accessToken.containsKey(email)) {
            throw new NoSuchElementException(ErrorMessage.KAKAO_NO_SUCH_TOKEN_MSG);
        }
        return accessToken.get(email);
    }
}