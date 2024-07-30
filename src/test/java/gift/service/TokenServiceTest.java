package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.AuthToken;
import gift.domain.TokenInformation;
import gift.exception.customException.EmailDuplicationException;
import gift.exception.customException.UnAuthorizationException;
import gift.repository.token.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static gift.utils.TokenConstant.EXPIRATION_OFFSET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    @DisplayName("토큰 저장 시 이메일 중복 테스트")
    void 토큰_저장_이메일_중복_테스트(){
        //given
        String token = UUID.randomUUID().toString();
        String duplicationEmail = "abcd@pusan.ac.kr";

        given(tokenRepository.findTokenByEmail(duplicationEmail)).willReturn(Optional.of(new AuthToken(token, duplicationEmail)));

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> tokenService.tokenSave(token, duplicationEmail))
                        .isInstanceOf(EmailDuplicationException.class)
        );

    }

    @Test
    @DisplayName("토큰 저장 테스트")
    void 토큰_저장_테스트(){
        //given
        String token = UUID.randomUUID().toString();
        String email = "abc@pusan.ac.kr";

        given(tokenRepository.findTokenByEmail(email)).willReturn(Optional.empty());

        //when
        String savedToken = tokenService.tokenSave(token, email);

        //then
        assertAll(
                () -> assertThat(savedToken).isEqualTo(token),
                () -> verify(tokenRepository, times(1)).save(any(AuthToken.class))
        );
    }

    @Test
    @DisplayName("토큰을 이용한 토큰 조회 시 토큰 조회 실패 테스트")
    void 토큰_토큰_조회_조회_실패_테스트(){
        //given
        String nullToken = UUID.randomUUID().toString();

        given(tokenRepository.findAuthTokenByToken(nullToken)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> tokenService.findToken(nullToken))
                        .isInstanceOf(UnAuthorizationException.class)
        );
    }

    @Test
    @DisplayName("토큰을 이용한 토큰 조회 테스트")
    void 토큰_토큰_조회_테스트(){
        //given
        String token = UUID.randomUUID().toString();
        String email = "abc@pusan.ac.kr";

        given(tokenRepository.findAuthTokenByToken(token)).willReturn(Optional.of(new AuthToken(token, email)));

        //when
        AuthToken findAuthToken = tokenService.findToken(token);

        //then
        assertAll(
                () -> assertThat(findAuthToken.getToken()).isEqualTo(token),
                () -> assertThat(findAuthToken.getEmail()).isEqualTo(email)
        );
    }

    @Test
    @DisplayName("OAuth 토큰 저장 테스트")
    void OAuth_토큰_저장_테스트() throws Exception{
        //given
        String email = "123@kakao.com";

        String response = "{" +
                "\"access_token\": \"Test Token\"," +
                "\"expires_in\": 30000," +
                "\"refresh_token\": \"Test Refresh Token\"," +
                "\"refresh_token_expires_in\": 70000" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response);

        TokenInformation tokenInfo = new TokenInformation(jsonNode);

        AuthToken authToken = new AuthToken.Builder()
                .token("test token")
                .tokenTime(tokenInfo.getAccessTokenTime() - EXPIRATION_OFFSET)
                .email(email)
                .accessToken(tokenInfo.getAccessToken())
                .accessTokenTime(tokenInfo.getAccessTokenTime())
                .refreshToken(tokenInfo.getRefreshToken())
                .refreshTokenTime(tokenInfo.getRefreshTokenTime())
                .build();

        given(tokenRepository.save(any(AuthToken.class))).willReturn(authToken);

        //when
        AuthToken savedToken = tokenService.oauthTokenSave(tokenInfo, email);

        //then
        assertAll(
                () -> assertThat(savedToken.getAccessToken()).isEqualTo("Test Token"),
                () -> assertThat(savedToken.getRefreshToken()).isEqualTo("Test Refresh Token")
        );
    }

    @Test
    @DisplayName("OAuth 토큰 수정 테스트")
    void OAuth_토큰_수정_테스트() throws Exception{
        //given
        String email = "123@kakao.com";

        String response = "{" +
                "\"access_token\": \"Test Token\"," +
                "\"expires_in\": 30000," +
                "\"refresh_token\": \"Test Refresh Token\"," +
                "\"refresh_token_expires_in\": 70000" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response);

        TokenInformation tokenInfo = new TokenInformation(jsonNode);

        AuthToken authToken = new AuthToken.Builder()
                .token("test token")
                .tokenTime(tokenInfo.getAccessTokenTime() - EXPIRATION_OFFSET)
                .email(email)
                .accessToken("하하하")
                .accessTokenTime(tokenInfo.getAccessTokenTime())
                .refreshToken("호호호")
                .refreshTokenTime(tokenInfo.getRefreshTokenTime())
                .build();

        Field idField = AuthToken.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(authToken, 1L);

        given(tokenRepository.findById(anyLong())).willReturn(Optional.of(authToken));

        //when
        AuthToken updateToken = tokenService.updateToken(authToken.getId(), tokenInfo);

        //then
        assertAll(
                () -> assertThat(updateToken.getAccessToken()).isEqualTo("Test Token"),
                () -> assertThat(updateToken.getRefreshToken()).isEqualTo("Test Refresh Token")
        );
    }
}