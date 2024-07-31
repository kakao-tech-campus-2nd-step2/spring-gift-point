package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void registMemberTest() {
        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        Optional<Member> registMember = memberRepository.findByEmail("admin@gmail.com");
        assertThat(registMember).isPresent();
        assertThat(registMember.get().getEmail()).isEqualTo("admin@gmail.com");

    }

    @Test
    @DisplayName("유저 삭제 테스트")
    public void deleteMemberTest(){

        Member member = new Member("admin@gmail.com", "password");
        memberRepository.save(member);

        memberRepository.delete(member);

        Optional<Member> deleteMember = memberRepository.findByEmail("admin@gmail.com");
        assertThat(deleteMember).isEmpty();
    }

    @Test
    void save() {
        Member expected = new Member("admin@gmail.com", "password");
        Member actual = memberRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findByEmail() {
        String expected = "admin@gmail.com";
        memberRepository.save(new Member(expected, "password"));
        Member actual = memberRepository.findByEmail(expected).orElse(null);
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expected);
    }

}
