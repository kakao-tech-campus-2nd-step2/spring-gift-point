package gift.repository;

import gift.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member expected = new Member("test@example.com", "password");
        Member actual = memberRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @Test
    void findByEmail() {
        String expectedEmail = "test@example.com";
        memberRepository.save(new Member(expectedEmail, "password"));

        Optional<Member> actual = memberRepository.findByEmail(expectedEmail);
        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(expectedEmail);
    }
}
