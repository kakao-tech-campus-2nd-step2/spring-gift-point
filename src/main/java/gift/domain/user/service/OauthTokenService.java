package gift.domain.user.service;

import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.entity.User;
import gift.domain.user.repository.OauthTokenJpaRepository;
import gift.exception.InvalidAuthException;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Service;

@Service
public class OauthTokenService {

    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider;

    public OauthTokenService(
        OauthTokenJpaRepository oauthTokenJpaRepository,
        OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider
    ) {
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.oauthApiProvider = oauthApiProvider;
    }

    public OauthToken getOauthToken(User user, AuthProvider provider) {
        OauthToken oauthToken = oauthTokenJpaRepository.findByUserAndProvider(user, provider)
            .orElseThrow(() -> new InvalidAuthException("error.invalid.token"));
        return renew(oauthToken);
    }

    private OauthToken renew(OauthToken oauthToken) {
        KakaoToken kakaoToken = oauthApiProvider.renewToken(oauthToken.getRefreshToken());

        oauthToken.updateInfo(kakaoToken.accessToken(), kakaoToken.refreshToken());
        return oauthTokenJpaRepository.save(oauthToken);
    }
}
