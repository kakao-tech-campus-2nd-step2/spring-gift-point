package gift.repository;

import static gift.util.MemberFixture.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Member;
import gift.exception.NoSuchMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 추가")
    @Test
    void save() {
        // given
        Member expected = createMember();

        // when
        Member actual = memberRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @DisplayName("이메일로 회원 찾기")
    @Test
    void findById() {
        // given
        Member expected = memberRepository.save(createMember());

        // when
        Member actual = memberRepository.findById(expected.getEmail())
            .orElseThrow(NoSuchMemberException::new);

        // then
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }

    @DisplayName("회원 비밀번호 수정")
    @Test
    void update() {
        // given
        memberRepository.save(createMember());
        Member expected = createMember("test");

        // when
        Member actual = memberRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }
}
