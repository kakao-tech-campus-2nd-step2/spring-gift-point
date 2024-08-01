package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.member.MemberRepository;
import gift.member.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmail() {
        Member expected = new Member("member1@example.com", "password1", "member1", "user");
        memberRepository.save(expected);

        Member actual = memberRepository.findByEmail(expected.getEmail());

        assertThat(actual).isEqualTo(expected);
    }
}
