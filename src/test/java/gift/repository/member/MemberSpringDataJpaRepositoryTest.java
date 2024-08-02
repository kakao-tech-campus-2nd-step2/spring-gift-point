package gift.repository.member;

import gift.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberSpringDataJpaRepositoryTest {

    @Autowired
    private MemberSpringDataJpaRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member = new Member("test@example.com", "password", NORMAL);
        memberRepository.save(member);
    }

    @Test
    public void testSaveMember() {
        Optional<Member> foundMember = memberRepository.findByEmailAndLoginType("test@example.com", NORMAL);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByEmailAndLoginType() {
        Optional<Member> foundMember = memberRepository.findByEmailAndLoginType("test@example.com", NORMAL);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

}
