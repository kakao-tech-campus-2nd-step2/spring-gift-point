package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberRepositoryTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    void save() {
        Member expected = new Member(null, "admin", "admin@gmail.com","1234");
        Member actual = memberRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUsername()).isEqualTo(expected.getUsername()),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @Test
    void deleteById() {
        Long id = 1L;
        Member expected = new Member(id, "admin", "admin@gmail.com","1234");
        memberRepository.save(expected);
        memberRepository.deleteById(id);

        assertThat(memberRepository.findById(id)).isEmpty();
    }

}
