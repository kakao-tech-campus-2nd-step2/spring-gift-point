package gift.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class KakaoAccessTokenRepository {

    private static final Map<Long, String> database = new HashMap<>();

    public void saveAccessToken(Long memberId, String accessToken) {
        database.put(memberId, accessToken);
    }

    public String getAccessToken(Long memberId) {
        return database.get(memberId);
    }
}
