package gift.service;

import gift.domain.LoginType;
import gift.dto.request.MemberRequest;
import gift.domain.Member;
import gift.dto.response.KakaoLoginResponse;
import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.PointResponse;
import gift.exception.DuplicateMemberEmailException;
import gift.exception.InvalidCredentialsException;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static gift.domain.LoginType.KAKAO;
import static gift.domain.LoginType.NORMAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberSpringDataJpaRepository memberRepository;

    @Mock
    private KakaoAuthService kakaoAuthService;

    @Mock
    private TokenService tokenService;

    @Test
    public void 회원_등록_성공() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        when(memberRepository.findByEmailAndLoginType(any(String.class), any(LoginType.class))).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = memberService.register(memberRequest, NORMAL);

        assertNotNull(member);
        assertEquals("test@example.com", member.getEmail());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void 중복_이메일로_인한_등록_실패() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(new Member()));

        assertThrows(DuplicateMemberEmailException.class, () -> {
            memberService.register(memberRequest, NORMAL);
        });
    }

    @Test
    public void 정상_로그인_성공() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        Member member = mock(Member.class);

        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(member));
        when(member.getPassword()).thenReturn("password");
        when(member.getEmail()).thenReturn("test@example.com");

        Member authenticatedMember = memberService.authenticate(memberRequest, LoginType.NORMAL);

        assertNotNull(authenticatedMember);
        assertEquals("test@example.com", authenticatedMember.getEmail());
        verify(memberRepository, times(1)).findByEmailAndLoginType(any(String.class), any(LoginType.class));
    }

    @Test
    public void 잘못된_비밀번호로_인한_로그인_실패() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "wrongpassword");
        Member member = mock(Member.class);

        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(member));
        when(member.getPassword()).thenReturn("password");

        assertThrows(InvalidCredentialsException.class, () -> {
            memberService.authenticate(memberRequest, NORMAL);
        });
    }

    @Test
    public void 등록되지않은_이메일로_인한_로그인_실패() {
        MemberRequest memberRequest = new MemberRequest("nonexistent@example.com", "password");

        when(memberRepository.findByEmailAndLoginType("nonexistent@example.com", NORMAL)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.authenticate(memberRequest, NORMAL);
        });
    }

    @Test
    void 카카오_로그인_성공() {
        String authorizationCode = "test-authorization-code";
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";
        String email = "test-nickname@kakao.com";
        String nickname = "test-nickname";

        KakaoTokenResponse kakaoTokenResponse = new KakaoTokenResponse(
                "bearer",
                accessToken,
                3600,
                refreshToken,
                3600 * 24 * 30,
                "account_email"
        );

        KakaoProfileResponse kakaoProfileResponse = new KakaoProfileResponse(
                new KakaoProfileResponse.KakaoAccount(
                        new KakaoProfileResponse.KakaoAccount.Profile(nickname)
                )
        );

        MemberRequest memberRequest = new MemberRequest(email, "kakao");
        Member member = new Member(email, "kakao", KAKAO);

        when(kakaoAuthService.getKakaoToken(authorizationCode)).thenReturn(kakaoTokenResponse);
        when(kakaoAuthService.getUserProfile(accessToken)).thenReturn(kakaoProfileResponse);
        when(memberRepository.findByEmailAndLoginType(email, KAKAO)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(tokenService.saveToken(any(Member.class), eq(accessToken))).thenReturn(accessToken);

        KakaoLoginResponse kakaoLoginResponse = memberService.handleKakaoLogin(authorizationCode);

        assertNotNull(kakaoLoginResponse);
        assertEquals(email, kakaoLoginResponse.getEmail());
        assertEquals(authorizationCode, kakaoLoginResponse.getAuthorizationCode());
        assertEquals(accessToken, kakaoLoginResponse.getAccessToken());

        verify(kakaoAuthService, times(1)).getKakaoToken(authorizationCode);
        verify(kakaoAuthService, times(1)).getUserProfile(accessToken);
        verify(memberRepository, times(1)).findByEmailAndLoginType(email, KAKAO);
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(tokenService, times(1)).saveToken(any(Member.class), eq(accessToken));
    }

    @Test
    void 포인트_조회_성공() {
        Long memberId = 1L;
        Member member = new Member("test@example.com", "password", LoginType.NORMAL);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        PointResponse pointResponse = memberService.getPoint(memberId);

        assertNotNull(pointResponse);
        assertEquals(member.getPoint(), pointResponse.getPoint());
        verify(memberRepository, times(1)).findById(memberId);
    }

}
