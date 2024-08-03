package gift.repository;

import gift.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        testMember = new Member();
        testMember.setEmail("test@example.com");
        testMember.setPassword("password");
        memberRepository.save(testMember);
    }

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    public void testFindByEmail_Success() {
        Optional<Member> foundMember = memberRepository.findByEmail("test@example.com");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    public void testFindByEmail_NotFound() {
        Optional<Member> foundMember = memberRepository.findByEmail("notfound@example.com");
        assertThat(foundMember).isNotPresent();
    }

    @Test
    public void testSaveMember() {
        Member newMember = new Member();
        newMember.setEmail("new@example.com");
        newMember.setPassword("newpassword");

        Member savedMember = memberRepository.save(newMember);

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("new@example.com");
    }
}
