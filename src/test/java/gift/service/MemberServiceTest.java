package gift.service;

import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import gift.exception.InvalidLoginInfoException;
import gift.reflection.AuthTestReflectionComponent;
import gift.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthTestReflectionComponent authTestReflectionComponent;

    @Test
    @DisplayName("회원 탈퇴하기 - 성공")
    void successDeleteMember() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", "MEMBER");
        var loginRequest = new LoginRequest("test@naver.com", "testPassword");
        authService.register(registerRequest);
        var loginAuth = authService.login(loginRequest);
        var id = authTestReflectionComponent.getMemberIdWithToken(loginAuth.token());
        //when
        memberService.deleteMember(id);
        //then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidLoginInfoException.class);
    }
}
