package gift;

import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.dto.Role;  // Role enum을 import 합니다.
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testFindByEmail() {
        // Given
        Member member = new Member("test@example.com", "password123");
        memberRepository.save(member);

        // When
        Member foundMember = memberRepository.findByEmail("test@example.com");

        // Then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
        assertThat(foundMember.getPassword()).isEqualTo("password123");
        assertThat(foundMember.getRole()).isEqualTo(Role.USER);
    }

}
