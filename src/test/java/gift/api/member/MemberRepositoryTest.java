package gift.api.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.api.member.domain.Member;
import gift.api.member.enums.Role;
import gift.api.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    private final String EMAIL = "member@test.com";
    private final String PASSWORD = "passWd12";
    private final Role ROLE = Role.ADMIN;

    @Test
    void save() {
        Member expected = new Member(EMAIL, PASSWORD, ROLE);
        Member actual = memberRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void existsByEmail() {
        boolean expected = true;
        memberRepository.save(new Member(EMAIL, PASSWORD, ROLE));
        boolean actual = memberRepository.existsByEmail(EMAIL);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void existsByEmailAndPassword() {
        boolean expected = true;
        memberRepository.save(new Member(EMAIL, PASSWORD, ROLE));
        boolean actual = memberRepository.existsByEmailAndPassword(EMAIL, PASSWORD);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByEmail() {
        String expected = EMAIL;
        memberRepository.save(new Member(expected, PASSWORD, ROLE));
        String actual = memberRepository.findByEmail(expected).get().getEmail();
        assertThat(actual).isEqualTo(expected);
    }
}