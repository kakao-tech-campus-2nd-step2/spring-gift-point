package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import gift.exception.member.NotFoundMemberException;
import gift.exception.member.DuplicateEmailException;
import gift.model.Member;
import gift.response.MemberInfoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    Member member = new Member("aaa123@2a.com", "1234");


    @Test
    @DisplayName("회원가입 성공 테스트")
    void join() {
        assertDoesNotThrow(() -> memberService.join(member.getEmail(), member.getPassword()));
    }

    @Test
    @DisplayName("회원가입 실패 테스트:중복된 이메일")
    void failJoin() {
        memberService.join(member.getEmail(), member.getPassword());
        Assertions.assertThatThrownBy(
            () -> memberService.join(member.getEmail(), member.getPassword())
        ).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login() {
        Member joinedMember = memberService.join(member.getEmail(), member.getPassword());
        assertDoesNotThrow(() -> memberService.login(joinedMember.getEmail(), joinedMember.getPassword()));
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void failLogin() {
        Member joinedMember = memberService.join(member.getEmail(), member.getPassword());

        assertThatThrownBy(
            () -> memberService.login("wrong email", joinedMember.getPassword())
        ).isInstanceOf(NotFoundMemberException.class);

        assertThatThrownBy(
            () -> memberService.login(joinedMember.getEmail(), "wrong password")
        ).isInstanceOf(NotFoundMemberException.class);
        assertThatThrownBy(
            () -> memberService.login("wrong email", "wrong password")
        ).isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    @DisplayName("나의 정보 조회 테스트")
    void getInfo() {
        Member member = memberService.join(this.member.getEmail(), this.member.getPassword());

        MemberInfoResponse memberInfo = memberService.getMemberInfo(member.getId());
        assertThat(memberInfo).isNotNull();
        assertThat(memberInfo.email()).isEqualTo(member.getEmail());
        assertThat(memberInfo.remainPoint()).isEqualTo(0);
    }


}