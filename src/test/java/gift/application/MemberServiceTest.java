package gift.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.client.KakaoClient;
import gift.member.application.MemberService;
import gift.member.dao.MemberRepository;
import gift.member.entity.KakaoTokenInfo;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private KakaoClient kakaoClient;

    @Test
    @DisplayName("회원가입 서비스 테스트")
    void registerMember() {
        MemberRequest memberRequest = new MemberRequest("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberRequest);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());
        given(memberRepository.save(any()))
                .willReturn(member);

        memberService.registerMember(memberRequest);

        verify(memberRepository).findByEmail(memberRequest.email());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void registerMemberFailed() {
        MemberRequest memberRequest = new MemberRequest("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberRequest);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.registerMember(memberRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_ALREADY_EXISTS
                                     .getMessage());
    }


    @Test
    @DisplayName("회원 조회 기능 테스트")
    void getMemberById() {
        Long memberId = 1L;
        Member member = MemberFixture.createMember("test@email.com");
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        Member foundMember = memberService.getMemberByIdOrThrow(memberId);

        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("카카오 토큰 갱신 기능 테스트")
    void refreshKakaoAccessToken() {
        Long memberId = 1L;
        Member member = new Member(
                "test@email.com",
                "password",
                new KakaoTokenInfo(
                        "token",
                        LocalDateTime.now(),
                        "refresh-token"
                )
        );

        KakaoTokenResponse response = new KakaoTokenResponse(
                "token",
                "test-token",
                3600,
                "test-refresh-token",
                3600,
                "code"
        );

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(kakaoClient.getRefreshTokenResponse(any()))
                .willReturn(response);

        memberService.refreshKakaoAccessToken(memberId);
        assertThat(member.getKakaoAccessToken()).isEqualTo(response.accessToken());
        assertThat(member.getKakaoRefreshToken()).isEqualTo(response.refreshToken());
    }

}