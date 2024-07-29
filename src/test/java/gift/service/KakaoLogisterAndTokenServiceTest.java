package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import gift.dto.betweenClient.member.MemberDTO;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoLogisterAndTokenServiceTest {

    @Mock
    private KakaoTokenService kakaoTokenService;

    @Mock
    private MemberService memberService;

    private KakaoAuthService kakaoAuthService;

    private MemberDTO memberDTO;
    private MemberDTO memberDTOInDb;

    @BeforeEach
    void setUp() {
        kakaoAuthService = new KakaoAuthService(kakaoTokenService, memberService);
        memberDTO = new MemberDTO("1234@1234.com", "1234", "social", "nickname");
        memberDTOInDb = new MemberDTO("1234@1234.com", "1234", "basic", "nickname");
    }

    @Test
    void register(){
        given(kakaoTokenService.getAccessToken(any())).willReturn("토큰");
        given(kakaoTokenService.getUserInfo(any())).willReturn(memberDTO);
        given(memberService.getMember(any())).willThrow(new UserNotFoundException("없는 유저 입니다."));

        assertThat(kakaoAuthService.loginOrRegister("아무 인가 코드")).isEqualTo(memberDTO);
        verify(memberService).register(memberDTO);
    }

    @Test
    void testEmailDuplicated() {
        given(kakaoTokenService.getAccessToken(any())).willReturn("토큰");
        given(kakaoTokenService.getUserInfo(any())).willReturn(memberDTO);
        given(memberService.getMember(any())).willReturn(memberDTOInDb);

        assertThrows(BadRequestException.class, () -> kakaoAuthService.loginOrRegister("아무 인가 코드"));
    }

    @Test
    void logister_WhenTokenServiceThrowsException_ShouldPropagateException() {
        given(kakaoTokenService.getAccessToken(any())).willThrow(new BadRequestException("Invalid token"));

        assertThrows(BadRequestException.class, () -> kakaoAuthService.loginOrRegister("아무 인가 코드"));
    }
}