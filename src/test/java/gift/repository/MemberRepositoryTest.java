package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final String email = "test@email.com";
    private final String password = "mypassword";

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void save() {
        // Given
        var member = new Member(email, password);

        // When
        memberRepository.save(member);

        // Then
        var savedMember = memberRepository.findAll().getFirst();
        assertThat(savedMember).isNotNull();
        assertAll(
            () -> assertThat(savedMember.getEmail()).isEqualTo(email),
            () -> assertThat(savedMember.getPassword()).isEqualTo(password)
        );
    }

    @Test
    void findByEmail() {
        // Given
        var member = new Member(email, password);
        memberRepository.save(member);

        // When
        var foundMember = memberRepository.findByEmail(email);

        // Then
        assertThat(foundMember).isPresent();
        assertAll(
            () -> assertThat(foundMember.get().getEmail()).isEqualTo(email),
            () -> assertThat(foundMember.get().getPassword()).isEqualTo(password)
        );
    }

    @Test
    void deleteTest() {
        // Given
        var member = new Member(email, password);
        memberRepository.save(member);

        // When
        memberRepository.delete(member);

        // Then
        assertThat(memberRepository.findAll()).isEmpty();
    }
}