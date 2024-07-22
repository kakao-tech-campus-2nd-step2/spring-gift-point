package gift.global.security;

import gift.auth.domain.AuthInfo;

public interface TokenManager {
    String createAccessToken(AuthInfo authInfo);

    // token에 저장된 정보에서 AuthInfo를 추출하는 함수
    AuthInfo getParsedClaims(String token);
}
