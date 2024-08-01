package gift.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @BeforeEach
    void setUp() {
        repository.save(new Member("tester@gmail.com", "더미", "password", 1));
    }


    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        Member expected = new Member("test@gmail.com", "영식", "password", 1);

        Member actual = repository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("findByEmail 테스트")
    void findByEmail() {
        Member expected = new Member("tester@gmail.com", "더미", "password", 1);

        Member actual = repository.findByEmail(expected.getEmail()).orElse(null);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }
}
