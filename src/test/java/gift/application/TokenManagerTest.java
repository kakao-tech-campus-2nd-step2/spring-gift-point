package gift.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.application.token.TokenManager;
import gift.application.token.apicaller.KakaoTokenApiCaller;
import gift.model.token.KakaoToken;
import gift.repository.token.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TokenRepository tokenRepository;

    private final String accessToken = "-Ab9L-4PzjY9uoV5qI5gY6fv5crIbIZnAAAAAQopyV4AAAGQ7c5ITtQ0RDl69jWm";
    private final String refreshToken = "ZvbeJjPw7snkfywIcyx7x7n9TC3J1yK_AAAAAgoqJVEAAAGQ7c3LeNQ0RDl69jWm";
    @Autowired
    private KakaoTokenApiCaller kakaoTokenApiCaller;

    @Test
    @DisplayName("AccessToken_재발급_테스트")
    void AccessToken_재발급_테스트() {
        // given
        KakaoToken kakaoToken = new KakaoToken(accessToken, "Bearer", refreshToken, 0);
        tokenRepository.saveToken(1L, kakaoToken);

        // when
        KakaoToken newToken = kakaoTokenApiCaller.refreshAccessToken(kakaoToken.getRefreshToken());
        // then
        assertThat(newToken.getAccessToken()).isNotEqualTo(kakaoToken.getAccessToken());
        System.out.println("new token = " + newToken.getAccessToken());
        System.out.println("origin token = " + kakaoToken.getAccessToken());
        System.out.println("new refresh token = " + newToken.getRefreshToken());
        System.out.println("origin refresh token = " + kakaoToken.getRefreshToken());
    }
}
