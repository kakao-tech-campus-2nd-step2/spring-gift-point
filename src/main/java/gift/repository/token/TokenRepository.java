package gift.repository.token;

import gift.model.token.KakaoToken;

public interface TokenRepository {

    public void saveToken(Long userId, KakaoToken token);

    public KakaoToken getToken(Long userId);
}
