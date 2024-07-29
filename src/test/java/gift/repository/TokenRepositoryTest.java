package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.token.Token;
import gift.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;
    private Token token;

    @BeforeEach
    void beforeEach(){
        token = new Token(1L, "kakao", "1234", 360000, "5678",
            360000);
    }

    @Test
    @DisplayName("토큰 저장")
    void save(){
        //given
        Token expected = new Token(1L, "kakao", "1234", 360000, "5678",
            360000);

        //when
        Token actual = tokenRepository.save(token);

        //then
        assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());
    }

    @Test
    @DisplayName("회원 아이디와 sns로 토큰 찾기")
    void findByUserIdAndSns(){
        //given
        Token expected = new Token(1L, "kakao", "1234", 360000, "5678",
            360000);
        tokenRepository.save(token);

        //when
        Token actual = tokenRepository.findByUserIdAndSns(1L, "kakao");

        //then
        assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());
    }

    @Test
    @DisplayName("회원 아이디와 sns로 존재 여부 확인 시 존재함")
    void existsByUserIdAndSns(){
        //given
        Token expected = new Token(1L, "kakao", "1234", 360000, "5678",
            360000);
        tokenRepository.save(token);

        //when
        boolean actual = tokenRepository.existsByUserIdAndSns(1L, "kakao");

        //then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 아이디와 sns로 존재 여부 확인 시 존재하지 않음")
    void notExistsByUserIdAndSns(){
        //given
        Token expected = new Token(1L, "kakao", "1234", 360000, "5678",
            360000);
        tokenRepository.save(token);

        //when
        boolean actual = tokenRepository.existsByUserIdAndSns(2L, "kakao");

        //then
        assertThat(actual).isEqualTo(false);
    }
}
