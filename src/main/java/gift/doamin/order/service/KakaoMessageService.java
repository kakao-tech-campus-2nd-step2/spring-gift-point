package gift.doamin.order.service;

import gift.doamin.order.util.KakaoMessageRestClient;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.repository.KakaoOAuthTokenRepository;
import gift.doamin.user.service.OAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoMessageService {

    private final KakaoOAuthTokenRepository oAuthTokenRepository;
    private final OAuthService oAuthService;
    private final KakaoMessageRestClient kakaoMessageRestClient;

    public KakaoMessageService(KakaoOAuthTokenRepository oAuthTokenRepository,
        OAuthService oAuthService, KakaoMessageRestClient kakaoMessageRestClient) {
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthService = oAuthService;
        this.kakaoMessageRestClient = kakaoMessageRestClient;
    }

    @Transactional
    public void sendMessage(User receiver, String message) {
        KakaoOAuthToken kakaoOAuthToken = oAuthTokenRepository.findByUser(receiver)
            .orElseThrow(() ->
                new IllegalArgumentException("카카오톡 사용자에게만 선물할 수 있습니다."));

        oAuthService.renewOAuthTokens(kakaoOAuthToken);

        kakaoMessageRestClient.sendMessage(kakaoOAuthToken.getAccessToken(), message);
    }

}
