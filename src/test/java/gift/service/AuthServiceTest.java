package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.AuthToken;
import gift.domain.Member;
import gift.domain.TokenInformation;
import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.exception.customException.EmailDuplicationException;
import gift.exception.customException.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TokenService tokenService;

    @Test
    @DisplayName("회원 가입 중 메일 중복 Exception 테스트")
    void 회원_가입_메일_중복_EXCEPTION_테스트(){
        //given
        Member member = new Member.Builder()
                .email("abc@pusan.ac.kr")
                .password("abc")
                .build();

        MemberRequestDto memberRequestDto = new MemberRequestDto(member.getEmail(), member.getPassword());
        given(memberRepository.findMemberByEmail(memberRequestDto.email())).willReturn(Optional.of(member));

        //when then
        assertThatThrownBy(() -> authService.memberJoin(memberRequestDto))
                .isInstanceOf(EmailDuplicationException.class);
    }

    @Test
    @DisplayName("회원 가입 정상 테스트")
    void 회원_가입_정상_테스트(){
        //given
        Member member = new Member.Builder()
                .email("abc@pusan.ac.kr")
                .password("abc")
                .build();

        MemberRequestDto memberRequestDto = new MemberRequestDto(member.getEmail(), member.getPassword());

        given(memberRepository.findMemberByEmail(memberRequestDto.email())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        MemberResponseDto returnMember = authService.memberJoin(memberRequestDto);

        //then
        assertAll(
                () -> assertThat(returnMember.email()).isEqualTo(member.getEmail()),
                () -> assertThat(returnMember.password()).isEqualTo(member.getPassword())
        );
    }

    @Test
    @DisplayName("이메일과 패스워드로 조회 시 NOT FOUND EXCEPTION 테스트")
    void 이메일_패스워드_조회_실패_테스트(){
        //given
        MemberRequestDto memberFailDto = new MemberRequestDto("테스트2@pusan.ac.kr", "abc");

        given(memberRepository.findMemberByEmailAndPassword(memberFailDto.email(), memberFailDto.password()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.findOneByEmailAndPassword(memberFailDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이메일과 패스워드로 조회 성공 테스트")
    void 이메일_패스워드_조회_성공_테스트(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("테스트@pusan.ac.kr", "abc");

        Member member = new Member.Builder()
                .email(memberRequestDto.email())
                .password(memberRequestDto.password())
                .build();

        given(memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), memberRequestDto.password()))
                .willReturn(Optional.of(member));

        //when
        MemberResponseDto findMemberDto = authService.findOneByEmailAndPassword(memberRequestDto);

        //then
        assertAll(
                () -> assertThat(findMemberDto.email()).isEqualTo(member.getEmail()),
                () -> assertThat(findMemberDto.password()).isEqualTo(member.getPassword())
        );
    }

    @Test
    @DisplayName("카카오 로그인 시 회원 등록 안되어 있어서 회원 가입 진행 테스트")
    void 카카오_로그인_회원_등록_X_회원_가입_테스트() throws Exception {
        //given
        String kakaoId = "123";

        String response = "{" +
                "\"access_token\": \"Test Token\"," +
                "\"expires_in\": 30000," +
                "\"refresh_token\": \"Test Refresh Token\"," +
                "\"refresh_token_expires_in\": 70000" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response);

        TokenInformation tokenInfo = new TokenInformation(jsonNode);

        Member member = new Member.Builder()
                .email(kakaoId+"@kakao.com")
                .password(kakaoId+"@kakao.com")
                .kakaoId(kakaoId)
                .build();

        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        AuthToken authToken = new AuthToken.Builder()
                .token("테스트 토큰")
                .tokenTime(3000)
                .accessToken("a1b2c3")
                .accessTokenTime(300000)
                .refreshToken("a2b3c4d5")
                .refreshTokenTime(300000)
                .build();

        given(memberRepository.findMemberByKakaoId(kakaoId)).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(tokenService.oauthTokenSave(tokenInfo, member.getEmail())).willReturn(authToken);

        //when
        String token = authService.kakaoMemberLogin(kakaoId, tokenInfo);

        //then
        assertAll(
                () -> assertThat(token).isEqualTo(authToken.getToken()),
                () -> verify(memberRepository,times(1)).save(any(Member.class))
        );
    }

    @Test
    @DisplayName("카카오 로그인 시 회원 등록이 되어 있어서 회원 가입 진행 X 테스트")
    void 카카오_로그인_회원_등록_O_회원_가입_X_테스트() throws Exception {
        //given
        String kakaoId = "123";

        String response = "{" +
                "\"access_token\": \"Test Token\"," +
                "\"expires_in\": 30000," +
                "\"refresh_token\": \"Test Refresh Token\"," +
                "\"refresh_token_expires_in\": 70000" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response);

        TokenInformation tokenInfo = new TokenInformation(jsonNode);

        Member member = new Member.Builder()
                .email(kakaoId+"@kakao.com")
                .password(kakaoId+"@kakao.com")
                .kakaoId(kakaoId)
                .build();

        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(member, 1L);

        AuthToken authToken = new AuthToken.Builder()
                .token("테스트 토큰")
                .tokenTime(3000)
                .accessToken("a1b2c3")
                .accessTokenTime(300000)
                .refreshToken("a2b3c4d5")
                .refreshTokenTime(300000)
                .build();

        given(memberRepository.findMemberByKakaoId(kakaoId)).willReturn(Optional.of(member));
        given(tokenService.oauthTokenSave(tokenInfo, member.getEmail())).willReturn(authToken);


        //when
        String token = authService.kakaoMemberLogin(kakaoId, tokenInfo);

        //then
        assertAll(
                () -> assertThat(token).isEqualTo(authToken.getToken())
        );
    }


}