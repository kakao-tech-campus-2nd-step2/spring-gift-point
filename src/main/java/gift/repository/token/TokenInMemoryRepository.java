package gift.repository.token;

import gift.model.token.KakaoToken;
import gift.model.token.Token;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TokenInMemoryRepository implements TokenRepository {

    private final Map<Long, Token> TokenStorage = new ConcurrentHashMap<>();

    @Override
    public void saveToken(Long userId, KakaoToken token) {
        TokenStorage.put(userId, token);
    }

    @Override
    public KakaoToken getToken(Long userId) {
        return (KakaoToken) TokenStorage.get(userId);
    }

}
