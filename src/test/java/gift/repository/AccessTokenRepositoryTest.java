package gift.repository;

import gift.config.RedissonConfig;
import gift.model.KakaoAccessToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Import(RedissonConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccessTokenRepositoryTest {
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Test
    @DisplayName("토큰 저장 테스트[성공]")
    void save() {
        // given
        Long memberId = 1L;
        String accessToken = "accessToken123";
        Long expiresIn = 20L;
        KakaoAccessToken Token = new KakaoAccessToken(memberId, accessToken, expiresIn);
        accessTokenRepository.save(Token);

        // when
        KakaoAccessToken actual = accessTokenRepository.findById(memberId).get();

        // then
        assertThat(actual.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("토큰 만료 테스트[성공]")
    void testTokenExpired() throws InterruptedException {
        // given
        Long memberId = 1L;
        String accessToken = "accessToken123";
        Long expiresIn = 5L;
        KakaoAccessToken Token = new KakaoAccessToken(memberId, accessToken, expiresIn);
        accessTokenRepository.save(Token);

        // when
        Thread.sleep(6000);

        // then
        assertThat(accessTokenRepository.findById(memberId)).isEmpty();
    }
}
