package gift.repositoryTest;

import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveAndFindMember() {
        Member member = new Member("테스트1@example.com", "password");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByEmail("테스트1@example.com");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("테스트1@example.com");
    }

    @Test
    void testFindByActiveToken() {
        Member member = new Member("테스트2@example.com", "password", "activeToken123");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByActiveToken("activeToken123");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getActiveToken()).isEqualTo("activeToken123");
    }
}
