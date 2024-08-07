package gift.repository;

import gift.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findMemberById() {
        Optional<Member> actualMember = memberRepository.findMemberById(1L);
        if (actualMember.isEmpty()) {
            fail("memberRepository.findMemberById Test Fail");
        }
        assertAll(
                () -> assertThat(actualMember.get().getId()).isNotNull(),
                () -> assertThat(actualMember.get().getEmail()).isEqualTo("member@kakao.com"),
                () -> assertThat(actualMember.get().getPassword()).isEqualTo("asdf")
        );
    }

    @Test
    void findMemberByEmail() {
        Optional<Member> actualMember = memberRepository.findMemberByEmail("member@kakao.com");
        if (actualMember.isEmpty()) {
            fail("memberRepository.findMemberByEmail Test Fail");
        }
        assertAll(
                () -> assertThat(actualMember.get().getId()).isNotNull(),
                () -> assertThat(actualMember.get().getEmail()).isEqualTo("member@kakao.com"),
                () -> assertThat(actualMember.get().getPassword()).isEqualTo("asdf")
        );
    }
}