package gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.auth.service.AuthenticationTool;
import gift.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationToolTest {

    AuthenticationTool authenticationTool = new AuthenticationTool();

    @Test
    @DisplayName("Member의 ID 가 NULL인 경우 실패")
    void makeToken() {
        //given
        Member member = new Member();

        //when then
        assertThatThrownBy(() -> authenticationTool.makeToken(member));

    }

    @Test
    void parseToken() {
    }
}