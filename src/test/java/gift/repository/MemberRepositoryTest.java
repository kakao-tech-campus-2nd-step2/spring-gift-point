package gift.repository;

import gift.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member expected = new Member("test@example.com", "password123");
        Member actual = memberRepository.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    void findByEmail() {
        String email = "test@example.com";
        memberRepository.save(new Member(email, "password123"));
        Optional<Member> actual = memberRepository.findByEmail(email);
        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }
}
