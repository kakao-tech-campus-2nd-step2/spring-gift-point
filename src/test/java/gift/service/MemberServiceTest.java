package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.authentication.token.JwtProvider;
import gift.authentication.token.Token;
import gift.domain.Member;
import gift.domain.vo.Email;
import gift.domain.vo.Password;
import gift.repository.MemberRepository;
import gift.repository.WishProductRepository;
import gift.web.dto.request.LoginRequest;
import gift.web.dto.request.member.CreateMemberRequest;
import gift.web.dto.response.LoginResponse;
import gift.web.dto.response.member.CreateMemberResponse;
import gift.web.dto.response.member.ReadMemberResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WishProductRepository wishProductRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("회원 생성 요청이 정상적일 때, 회원을 성공적으로 생성합니다.")
    void createMember() {
        //given
        CreateMemberRequest request = new CreateMemberRequest("member01@naver.com", "password01", "이름");
        given(memberRepository.save(any())).willReturn(
            new Member.Builder().id(1L).name(request.getName()).email(Email.from(request.getEmail())).build());

        //when
        CreateMemberResponse response = memberService.createMember(request);

        //then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getEmail()).isEqualTo(request.getEmail()),
            () -> assertThat(response.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    @DisplayName("회원 조회 요청이 정상적일 때, 회원을 성공적으로 조회합니다.")
    void readMember() {
        //given
        Member member = new Member.Builder().id(1L).name("이름").email(Email.from("member01@naver.com")).password(
            Password.from("password01")).build();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        //when
        ReadMemberResponse response = memberService.readMember(1L);

        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(member.getId()),
            () -> assertThat(response.getName()).isEqualTo(member.getName()),
            () -> assertThat(response.getEmail()).isEqualTo(member.getEmail().getValue())
        );
    }

    @Test
    @DisplayName("회원 삭제 요청이 정상적일 때, 회원을 성공적으로 삭제합니다.")
    void deleteMember() {
        //given
        Member member = new Member.Builder().id(1L).name("이름").email(Email.from("member01@naver.com")).password(
            Password.from("password01")).build();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        //when
        //then
        assertDoesNotThrow(() -> memberService.deleteMember(1L));
    }

    @Test
    @DisplayName("로그인 요청이 정상적일 때, 로그인을 성공적으로 수행합니다.")
    void login() {
        //given
        String email = "member01@naver.com";
        String password = "password01";
        Member member = new Member.Builder().id(1L).email(Email.from(email)).password(
            Password.from(password)).build();

        LoginRequest request = new LoginRequest(email, password);

        given(memberRepository.findByEmail(Email.from(email))).willReturn(Optional.of(member));
        given(jwtProvider.generateToken(member)).willReturn(Token.from("token"));

        //when
        LoginResponse response = memberService.login(request);

        //then
        assertThat(response.getAccessToken()).isNotNull();
    }
}