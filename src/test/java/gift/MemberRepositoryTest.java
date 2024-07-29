package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.Model.Member;
import gift.Repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save(){
        Member expected = new Member(1L,"1234@google.com","1234","");
        Member actual = memberRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }
    @Test
    void getMemberByEmail(){
        memberRepository.save(new Member(1L,"1234@google.com","1234",""));
        Member actual = memberRepository.findByEmail("1234@google.com");
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo("1234@google.com"),
            () -> assertThat(actual.getPassword()).isEqualTo("1234")
        );
    }
}
