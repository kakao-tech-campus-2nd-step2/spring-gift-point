package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository members;

    @Test
    void save() {
        var expected = createMember("test", "password");
        members.save(expected);
        assertThat(members.findAll()).isNotEmpty();
    }

    @Test
    void find() {
        var member = createMember("test", "password");
        Member expected = members.save(member);
        Member actual = members.findByEmailAndPassword(expected.getEmail(), expected.getPassword())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.MEMBER_NOT_FOUND, expected.getEmail(), expected.getPassword())
            );
        assertAll(
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword())
        );
    }

    private Member createMember(String email, String password) {
        return new Member(email, password);
    }
}
