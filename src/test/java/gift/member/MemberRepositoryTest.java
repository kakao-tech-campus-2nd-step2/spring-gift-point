package gift.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.member.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE member");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @DisplayName("[Unit] addMemberTest")
    void addMemberTest() {
        //given
        Member expect = new Member("aaa@email.com", "password");

        //when
        memberRepository.save(expect);
        Member actual = memberRepository.findById("aaa@email.com").get();

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("[Unit] existMemberByEmail test")
    void existMemberByEmailTest() {
        //given
        Member expect = new Member("aaa@email.com", "password");
        memberRepository.save(expect);

        //when
        boolean trueCase = memberRepository.existsById("aaa@email.com");
        boolean falseCase = memberRepository.existsById("bbb@email.com");

        //then
        assertAll(
            () -> assertThat(trueCase).isTrue(),
            () -> assertThat(falseCase).isFalse()
        );
    }

    @Test
    @DisplayName("[Unit] findMemberByEmail test")
    void findMemberByEmailTest() {
        //given
        Member expect = new Member("aaa@email.com", "password");
        memberRepository.save(expect);

        //when
        Member actual = memberRepository.findById("aaa@email.com").get();
        Optional<Member> errorCase = memberRepository.findById("bbb@email.com");

        //then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> assertThat(errorCase).isEmpty()
        );
    }
}