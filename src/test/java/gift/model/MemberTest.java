package gift.model;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthorizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class MemberTest {

    @Test
    @DisplayName("loginType 테스트[실패]-loginType이 다름")
    void checkLoginType() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password, "name", Role.USER, SocialLoginType.KAKAO);

        // when
        // then
        assertThatExceptionOfType(AuthorizationException.class)
                .isThrownBy(() -> member.checkLoginType(SocialLoginType.DEFAULT));
    }

    @Test
    @DisplayName("addPoint 테스트[성공]")
    void addTestSuccess() {
        // given
        int point = 1000;
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password, "name", Role.USER, SocialLoginType.KAKAO);

        // when
        member.addPoint(point);

        // then
        assertThat(member.getPoint()).isEqualTo(point);
    }

    @Test
    @DisplayName("usePoint 테스트[성공]")
    void usePointSuccess() {
        // given
        int point = 1000;
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password, "name", Role.USER, SocialLoginType.KAKAO);
        member.addPoint(point);

        // when
        member.usePoint(point);

        // then
        assertThat(member.getPoint()).isEqualTo(0);
    }

    @Test
    @DisplayName("usePoint 테스트[실패] - 포인트 부족")
    void usePointFail() {
        // given
        int point = 1000;
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password, "name", Role.USER, SocialLoginType.KAKAO);
        member.addPoint(point);

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> member.usePoint(point + 1));
    }
}