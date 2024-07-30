package gift.repository;

import gift.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testSaveAndFindMember() {
        Member member = new Member("test@example.com", "password123");
        Member savedMember = memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByEmail() {
        Member member = new Member("test@example.com", "password123");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByEmail("test@example.com");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }
}
