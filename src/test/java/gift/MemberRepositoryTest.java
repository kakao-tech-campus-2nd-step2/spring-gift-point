package gift;

import gift.domain.Member;
import gift.repository.MemberRepository;
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
        Member expected = new Member.MemberBuilder().email("test@example.com").password("password").build();
        Member actual = memberRepository.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    void findByEmail() {
        String expectedEmail = "test@example.com";
        memberRepository.save(new Member.MemberBuilder().email(expectedEmail).password("password").build());
        Optional<Member> actual = memberRepository.findByEmail(expectedEmail);
        assertThat(actual).isPresent();
        assertThat(actual.get().getEmail()).isEqualTo(expectedEmail);
    }
}
