package gift.repository;

import gift.model.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save(){
        Member expected = new Member("qwer@gmail.com","1234","root");
        Member actual = memberRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findByEmail() {
        Member expected = new Member("qwer@gmail.com","1234","root");
        memberRepository.save(expected);
        Member actual = memberRepository.findByEmail(expected.getEmail()).get();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete(){
        Member expected = new Member("qwer@gmail.com","1234","root");
        memberRepository.save(expected);
        memberRepository.delete(expected);
        Optional<Member> actual = memberRepository.findByEmail("qwer@gmail.com");
        assertThat(actual).isEmpty();
    }
}