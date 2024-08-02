//package gift.login;
//
//
//import gift.order.domain.Token;
//import gift.order.repository.TokenJPARepository;
//import gift.order.service.KakaoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class KakaoLoginTokenTest {
//
//    @Autowired
//    private KakaoService kakaoService;
//
//    @Autowired
//    private TokenJPARepository tokenJPARepository;
//
//    private String testAccessToken = "_Ve03IBfXAQrtdx2WRT8Me0gqXz-toEiAAAAAQo8I-gAAAGRDKu5FK3XznpenZPe";
//    private String testRefreshToken = "uMtdIW7yOdU7K9KKhc7GyRwf_YgrmTMRAAAAAgo8I-gAAAGRDKu5EK3XznpenZPe";
//    private String testUserName = "testUser";
//    private Long testExpiresIn = 21599L; // access token 만료 시간 (초)
//
//    @BeforeEach
//    public void setup() {
//        // 만료시간 설정
//        Long expiredTime = Instant.now().getEpochSecond() - testExpiresIn;
//
//        // 테스트용 토큰 엔티티 저장
//        Token token = new Token(testAccessToken, testRefreshToken, testUserName, testExpiresIn);
//        tokenJPARepository.save(token);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testValidateToken() {
//        boolean isValid = kakaoService.validateToken(testAccessToken);
//        assertThat(isValid).isTrue();
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void testRenewToken() {
//        kakaoService.renewToken(testAccessToken);
//        Token updatedToken = tokenJPARepository.findByRefreshToken(testRefreshToken);
//
//
//        assertThat(updatedToken.getAccessToken()).isNotEqualTo(testAccessToken);
//        assertThat(updatedToken.getRefreshToken()).isEqualTo(testRefreshToken);
//    }
//}
