package gift;

import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        Member member = new Member();
        member.setEmail("test@example.com");
        member.setPassword("password123");

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail() {
        String email = "test@example.com";
        Member member = new Member();
        member.setEmail(email);
        member.setPassword("password123");

        memberRepository.save(member);

        Member foundMember = memberRepository.findByEmail(email);

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(email);
    }
}