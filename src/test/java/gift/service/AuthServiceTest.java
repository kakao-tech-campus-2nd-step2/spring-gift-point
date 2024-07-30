package gift.service;

import gift.common.enums.Role;
import gift.controller.dto.request.SignInRequest;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("로그인 테스트[성공]")
    void signIn() {
        // given
        Long id = 1L;
        String email = "email@email.com";
        String password = "password";
        String token = "tokentoken";
        var request = new SignInRequest(email, password);
        Member member = new Member(id, email, password, Role.USER, null, null);
        given(memberRepository.findByEmailAndPassword(eq(email), eq(password)))
                .willReturn(Optional.of(member));
        given(jwtProvider.generateToken(eq(id), eq(member.getEmail()), eq(member.getRole())))
                .willReturn(token);

        // when
        String actual = authService.signIn(request).accessToken();

        // then
        assertThat(actual).isEqualTo(token);
    }
}