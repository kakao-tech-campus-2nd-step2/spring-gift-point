package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 및 ID로 조회")
    public void testSaveAndFindById() {
        Member member = new Member("test@example.com", "password");
        Member savedMember = memberRepository.save(member);
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("모든 회원 조회")
    public void testFindAll() {
        long initialCount = memberRepository.count();

        Member member1 = new Member("user1@example.com", "password1");
        Member member2 = new Member("user2@example.com", "password2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Iterable<Member> members = memberRepository.findAll();
        assertThat(members).hasSize((int) initialCount + 2);
    }

    @Test
    @DisplayName("회원 삭제")
    public void testDelete() {
        Member member = new Member("test@example.com", "password");
        Member savedMember = memberRepository.save(member);

        memberRepository.deleteById(savedMember.getId());
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        assertThat(foundMember).isNotPresent();
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    public void testFindByEmail() {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        Optional<Member> foundMember = memberRepository.findByEmail("test@example.com");

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("이메일 존재 여부 확인")
    public void testExistsByEmail() {
        Member member = new Member("test@example.com", "password");
        memberRepository.save(member);

        boolean exists = memberRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }
}
