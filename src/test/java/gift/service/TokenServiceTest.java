package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import gift.token.Token;
import gift.token.TokenRepository;
import gift.token.TokenService;
import gift.users.kakao.KakaoTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TokenServiceTest {

    private TokenService tokenService;
    private TokenRepository tokenRepository = mock(TokenRepository.class);

    @BeforeEach
    void beforeEach() {
        tokenService = new TokenService(tokenRepository);
    }

    @Test
    @DisplayName("토큰 저장")
    void saveToken() {
        //given
        KakaoTokenDTO kakaoTokenDTO =
            new KakaoTokenDTO("1", "1", 1, "1", 1);
        given(tokenRepository.existsByUserIdAndSns(anyLong(), anyString())).willReturn(false);

        //when
        tokenService.saveToken(1L, kakaoTokenDTO, "kakao");

        //then
        then(tokenRepository).should().save(any());
    }

    @Test
    @DisplayName("토큰 찾기")
    void findToken() {
        //given
        Token token =
            new Token(1L, "kakao", "access-token", 1, "refresh-token",1);
        given(tokenRepository.findByUserIdAndSns(anyLong(), anyString())).willReturn(token);

        //when
        String actual = tokenService.findToken(1L,"kakao");

        //then
        assertThat(actual).isEqualTo("access-token");
    }
}
