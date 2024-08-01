package gift.Product;


import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save(){
        String email = "test@gmail.com";
        String password = "test";
        Member expected = new Member(email,password);
        Member actual = memberRepository.save(expected);

        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    void login(){
        String email = "test100@gmail.com";
        String password = "test";
        Member expected = new Member(email,password);
        memberRepository.save(expected);
        Optional<Member> actual = memberRepository.findByEmail(email);
        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    void exists(){
        String email = "test200@gmail.com";
        String password = "test";
        Member expected = new Member(email,password);
        memberRepository.save(expected);
        boolean result = memberRepository.existsByEmail(email);
        assertThat(result).isTrue();
    }

}
