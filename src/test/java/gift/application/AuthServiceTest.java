package gift.application;

import gift.auth.application.AuthService;
import gift.auth.dto.AuthResponse;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.client.KakaoClient;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberRequest;
import gift.member.entity.Member;
import gift.member.util.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testFixtures.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private KakaoClient kakaoClient;

    @Test
    @DisplayName("회원 검증 서비스 테스트")
    void authenticate() {
        MemberRequest memberRequest = new MemberRequest("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberRequest);
        String token = "token";
        given(jwtUtil.generateToken(any()))
                .willReturn(token);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        AuthResponse authToken = authService.authenticate(memberRequest);

        assertThat(authToken.token()).isEqualTo(token);
    }

    @Test
    @DisplayName("존재하지 않는 회원 검증 실패 테스트")
    void authenticateMemberNotFound() {
        MemberRequest memberRequest = new MemberRequest("test@email.com", "password");
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(memberRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND
                                     .getMessage());
    }

    @Test
    @DisplayName("회원 비밀번호 검증 실패 테스트")
    void authenticateIncorrectPassword() {
        Member member = MemberFixture.createMember("test@email.com");
        MemberRequest memberRequest = new MemberRequest("test@email.com", "incorrect " + member.getPassword());
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> authService.authenticate(memberRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.AUTHENTICATION_FAILED
                                     .getMessage());
    }

    @Test
    @DisplayName("기존 회원 카카오 인가 코드 검증 테스트")
    void authenticateCodeExistingMember() {
        String code = "test-code";
        String token = "test-token";
        Long kakaoUserId = 123L;
        Member member = MemberFixture.createMember("kakao_user" + kakaoUserId + "@kakao.com");

        KakaoTokenResponse tokenResponse = new KakaoTokenResponse(
                "token",
                "test-token",
                3600,
                "test-refresh-token",
                3600,
                "code"
        );

        given(kakaoClient.getTokenResponse(anyString()))
                .willReturn(tokenResponse);
        given(kakaoClient.getUserId(anyString()))
                .willReturn(kakaoUserId);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        given(jwtUtil.generateToken(any()))
                .willReturn(token);

        AuthResponse authResponse = authService.authenticate(code);

        assertThat(authResponse.token()).isEqualTo(token);
    }

    @Test
    @DisplayName("신규 회원 카카오 인가 코드 검증 테스트")
    void authenticateCodeNewMember() {
        String code = "test-code";
        String token = "test-token";
        Long kakaoUserId = 123L;
        Member member = MemberFixture.createMember("kakao_user" + kakaoUserId + "@kakao.com");

        KakaoTokenResponse tokenResponse = new KakaoTokenResponse(
                "token",
                "test-token",
                3600,
                "test-refresh-token",
                3600,
                "code"
        );

        given(kakaoClient.getTokenResponse(anyString()))
                .willReturn(tokenResponse);
        given(kakaoClient.getUserId(anyString()))
                .willReturn(kakaoUserId);
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(memberRepository.save(any()))
                .willReturn(member);
        given(jwtUtil.generateToken(any()))
                .willReturn(token);

        AuthResponse authResponse = authService.authenticate(code);

        assertThat(authResponse.token()).isEqualTo(token);
    }

}