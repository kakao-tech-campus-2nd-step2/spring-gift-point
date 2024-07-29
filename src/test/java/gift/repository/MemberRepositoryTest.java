package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("user@example.com", "password");
        memberRepository.save(member);
    }

    @Test
    @DisplayName("이메일로 회원 찾기")
    void testFindByEmail() {
        Optional<Member> foundMember = memberRepository.findByEmail("user@example.com");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("이메일로 회원 찾기 실패")
    void testFindByEmail_NotFound() {
        Optional<Member> foundMember = memberRepository.findByEmail("noUser@example.com");
        assertThat(foundMember).isNotPresent();
    }

    @Test
    @DisplayName("새로운 회원 저장하기")
    void testSave() {
        Member newMember = new Member("newUser@example.com", "newPassword");
        Member savedMember = memberRepository.save(newMember);
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("newUser@example.com");
    }
}