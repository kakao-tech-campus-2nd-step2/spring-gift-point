package gift;

import gift.domain.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private static final String TEST_EMAIL = "test@gmail.com";
    private static final String TEST_PASSWORD = "password";

    @Test
    void save() {
        // given
        Member member = new Member(TEST_EMAIL, TEST_PASSWORD);

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(savedMember.getPassword()).isEqualTo(TEST_PASSWORD);
    }

    @Test
    void findByEmail() {
        // given
        Member member = new Member(TEST_EMAIL, TEST_PASSWORD);
        memberRepository.save(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail(TEST_EMAIL);

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(foundMember.get().getPassword()).isEqualTo(TEST_PASSWORD);
    }
}

