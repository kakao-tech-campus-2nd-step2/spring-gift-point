package gift.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import gift.domain.member.dto.MemberRequest;
import gift.domain.member.entity.AuthProvider;
import gift.auth.jwt.JwtToken;
import gift.auth.jwt.JwtProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberJpaRepository;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.entity.Role;
import gift.exception.InvalidUserInfoException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberJpaRepository memberJpaRepository;
    
    @MockBean
    private JwtProvider jwtProvider;
    

    @Test
    @DisplayName("회원 가입 서비스 테스트")
    void signUp_success() {
        // given
        MemberRequest memberRequest = new MemberRequest("test@test.com", "test123");

        Member member = memberRequest.toMember();
        given(memberJpaRepository.save(any(Member.class))).willReturn(member);

        JwtToken expectedJwtToken = new JwtToken("token");
        given(jwtProvider.generateToken(any(Member.class))).willReturn(expectedJwtToken);

        // when
        JwtToken actualJwtToken = memberService.signUp(memberRequest);

        // then
        assertThat(actualJwtToken).isEqualTo(expectedJwtToken);
    }

    @Test
    @DisplayName("로그인 서비스 테스트")
    void login_success() {
        // given
        MemberLoginRequest loginDto = new MemberLoginRequest("test@test.com", "test123");

        Member member = new Member(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
        given(memberJpaRepository.findByEmail(eq("test@test.com"))).willReturn(Optional.of(member));

        JwtToken expectedJwtToken = new JwtToken("token");
        given(jwtProvider.generateToken(any(Member.class))).willReturn(expectedJwtToken);

        // when
        JwtToken actualJwtToken = memberService.login(loginDto);

        // then
        assertThat(actualJwtToken).isEqualTo(expectedJwtToken);
    }

    @Test
    @DisplayName("로그인 서비스 테스트 - 존재하지 않는 이메일")
    void login_fail_email_error() {
        // given
        MemberLoginRequest loginDto = new MemberLoginRequest("test@test.com", "test123");

        given(memberJpaRepository.findByEmail(eq("test@test.com"))).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.login(loginDto))
            .isInstanceOf(InvalidUserInfoException.class)
            .hasMessage("error.invalid.userinfo.email");
    }

    @Test
    @DisplayName("로그인 서비스 테스트 - 틀린 비밀번호")
    void login_fail_password_error() {
        // given
        MemberLoginRequest loginDto = new MemberLoginRequest("test@test.com", "test123");

        Member member = mock(Member.class);
        given(memberJpaRepository.findByEmail(eq("test@test.com"))).willReturn(Optional.of(member));
        given(member.getAuthProvider()).willReturn(AuthProvider.LOCAL);
        given(member.checkPassword(eq("test123"))).willReturn(false);

        // when & then
        assertThatThrownBy(() -> memberService.login(loginDto))
            .isInstanceOf(InvalidUserInfoException.class)
            .hasMessage("error.invalid.userinfo.password");
    }
}