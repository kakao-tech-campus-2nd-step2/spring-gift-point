package gift.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        //given
        Member expected = new Member("test@test.test", "testpw");

        //when
        Member actual = memberRepository.save(expected);

        //then

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo("test@test.test"),
            () -> assertThat(actual.getPassword()).isEqualTo("testpw")

        );

    }

    @Test
    void findByEmailAndPassword() {
        //given
        Member expected = new Member("test@test.test", "testpw");
        memberRepository.save(expected);

        //when
        Member actual = memberRepository.findByEmailAndPassword("test@test.test", "testpw");

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())

        );

    }
}