package gift.repository;

import gift.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class JpaMemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 저장 테스트")
    void saveMember() {
        Member member = new Member(1L, "asdfasdf@naver.com", "asdfasdf");
        Member real = memberRepository.save(member);
        assertAll(
                () -> assertThat(real.getId()).isNotNull(),
                () -> assertThat(real.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(real.getPassword()).isEqualTo(member.getPassword())
        );
    }

    @Test
    @DisplayName("멤버를 이메일로 조회")
    void memberRepoFindByEmail() {
        String expected = "asdfasdf@naver.com";
        Member member = new Member(1L, expected, "asdfasdf");
        memberRepository.save(member);
        String actual = memberRepository.findByEmail(expected).get().getEmail();
        assertThat(actual).isEqualTo(expected);
    }
}
