package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Member;
import gift.repository.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private  MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("Create Test")
    void save() {
        // given
        Member expected = MemberFixture.createMember("donghyun","5434");

        // when
        Member actual = memberRepository.save(expected);
        em.flush();
        em.clear();
        // then
        assertAll(
            ()->assertThat(actual.getId()).isNotNull(),
            ()->assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            ()->assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @Test
    @DisplayName("Read By Email and Password")
    void findByEmailAndPassword() {
        // given
        Member expected = MemberFixture.createMember("donghyun","5434");
        memberRepository.save(expected);
        em.flush();
        em.clear();
        // when
        Member actual = memberRepository.findByEmailAndPassword(expected.getEmail(),expected.getPassword()).get();

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            ()->assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    @Test
    @DisplayName("Delete Test")
    void DeleteById(){
        // given
        Member expected = MemberFixture.createMember("donghyun","5434");
        memberRepository.save(expected);
        em.flush();
        em.clear();
        // when
        memberRepository.deleteById(expected.getId());
        Optional<Member> actual = memberRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }
}