package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.persistence.entity.Member;
import gift.member.persistence.repository.MemberJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberRepository;

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    void save() {
        // given
        Member expected = new Member("test@email.com", "test");

        // when
        Member actual = memberRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findByEmail() {
        // given
        Member expected = new Member("test@email.com", "test");
        Member saved = memberRepository.save(expected);

        // when
        Member actual = memberRepository.findByEmail(saved.getEmail()).orElse(null);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(saved.getEmail())
        );
    }

    @Test
    void findById() {
        // given
        Member expected = new Member("test@email.com", "test");
        Member saved = memberRepository.save(expected);

        // when
        Member actual = memberRepository.findById(saved.getId()).orElse(null);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(saved.getId())
        );
    }

}
