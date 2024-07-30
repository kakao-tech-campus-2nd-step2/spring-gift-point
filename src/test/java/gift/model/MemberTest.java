package gift.model;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("loginType 테스트[실패]-loginType이 다름")
    void checkLoginType() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password, Role.USER, SocialLoginType.KAKAO);

        // when
        // then
        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> member.checkLoginType(SocialLoginType.DEFAULT));
    }
}