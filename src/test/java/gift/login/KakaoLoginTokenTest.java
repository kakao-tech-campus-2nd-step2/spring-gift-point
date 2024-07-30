package gift.login;


import com.fasterxml.jackson.databind.ObjectMapper;
import gift.order.domain.Token;
import gift.order.dto.KakaoTokenResponse;
import gift.order.repository.TokenJPARepository;
import gift.order.service.KakaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KakaoLoginTokenTest {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private TokenJPARepository tokenJPARepository;

    private String testAccessToken = "p5dOKDUo_zjA_tD6TDi1O_wZ-uqVonulAAAAAQorDNQAAAGQ7gx3ia3XznpenZPe";
    private String testRefreshToken = "iqfZ4xfWkmDFq7WoA0zAOTVrN35KbNsrAAAAAgorDNQAAAGQ7gx3hq3XznpenZPe";
    private String testUserName = "testUser";
    private Long testExpiresIn = 21599L; // access token 만료 시간 (초)

    @BeforeEach
    public void setup() {
        // 만료시간 설정
        Long expiredTime = Instant.now().getEpochSecond() - testExpiresIn;

        // 테스트용 토큰 엔티티 저장
        Token token = new Token(testAccessToken, testRefreshToken, testUserName, testExpiresIn);
        tokenJPARepository.save(token);
    }

    @Test
    @Transactional
    @Rollback
    public void testValidateToken() {
        boolean isValid = kakaoService.validateToken(testAccessToken);
        assertThat(isValid).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void testRenewToken() {
        kakaoService.renewToken(testAccessToken);
        Token updatedToken = tokenJPARepository.findByRefreshToken(testRefreshToken);


        assertThat(updatedToken.getAccessToken()).isNotEqualTo(testAccessToken);
        assertThat(updatedToken.getRefreshToken()).isEqualTo(testRefreshToken);
    }
}
