package gift.repository.token;

import gift.domain.Member;
import gift.domain.TokenAuth;
import gift.repository.member.MemberSpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TokenSpringDataJpaRepositoryTest {

    @Autowired
    private TokenSpringDataJpaRepository tokenRepository;

    @Autowired
    private MemberSpringDataJpaRepository memberRepository;

    @Test
    public void testSaveToken() {
        Member member = new Member("test@example.com", "password", NORMAL);
        memberRepository.save(member);

        TokenAuth token = new TokenAuth("test-token", member);
        tokenRepository.save(token);

        Optional<TokenAuth> foundToken = tokenRepository.findByToken("test-token");
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getMember().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindToken() {
        Member member = new Member("test@example.com", "password", NORMAL);
        memberRepository.save(member);


        TokenAuth token = new TokenAuth("test-token", member);
        tokenRepository.save(token);
        tokenRepository.save(token);

        Optional<TokenAuth> foundToken = tokenRepository.findByToken("test-token");
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getMember().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testDeleteToken() {
        Member member = new Member("test@example.com", "password", NORMAL);
        memberRepository.save(member);

        TokenAuth token = new TokenAuth("test-token", member);
        tokenRepository.save(token);

        tokenRepository.delete(token);

        Optional<TokenAuth> foundToken = tokenRepository.findByToken("test-token");
        assertThat(foundToken).isNotPresent();
    }
}
