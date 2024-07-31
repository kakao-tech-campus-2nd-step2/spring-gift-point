package gift.repository.token;

import gift.common.enums.LoginType;
import gift.common.enums.TokenType;
import gift.model.token.OAuthToken;
import gift.model.user.User;
import gift.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OAuthTokenRepositoryTest {

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("OAuthToken이 잘 저장되고 조회되는지 테스트")
    public void testSaveAndFindByUser() {
        //given
        User user = new User("testEmail", "testPassword", "testName1", LoginType.KAKAO);
        userRepository.save(user);

        OAuthToken token = new OAuthToken(user, "testAccessToken", "testRefreshToken", TokenType.KAKAO);
        oAuthTokenRepository.save(token);

        //when
        Optional<OAuthToken> savedToken = oAuthTokenRepository.findByUser(user);

        //then
        assertThat(savedToken).isPresent();
        assertThat(savedToken.get().getAccessToken()).isEqualTo("testAccessToken");
        assertThat(savedToken.get().getRefreshToken()).isEqualTo("testRefreshToken");
        assertThat(savedToken.get().getTokenType()).isEqualTo(TokenType.KAKAO);
    }


}