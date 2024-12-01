package gift.repository;

import gift.vo.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;
    Member savedMember;

    @BeforeEach
    void setUp() {
        Member member = new Member("kookies@google.com", "@123");
        savedMember = repository.save(member);// Member pre-save
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll(); // Clean up after each test
    }

    @Test
    @DisplayName("Member save")
    void save() {
        // when
        Member actual = repository.save(new Member("newMember@google.com", "@kakaoTech"));

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo("newMember@google.com")
        );
    }

    @Test
    @DisplayName("Find All Member")
    void findAll() {
        // given
        Member member2 = repository.save(new Member("kaka5@google.com", "@ads5f4a"));

        // when
        List<Member> members = repository.findAll();

        // then
        assertAll(
                () ->  assertThat(members.size()).isEqualTo(2),
                () -> assertThat(members.contains(savedMember)).isTrue(),
                () -> assertThat(members.contains(member2)).isTrue()
        );
    }

    @Test
    @DisplayName("Find Member by id")
    void findById() {
        // given
        Long memberId = savedMember.getId();
        // when
        Optional<Member> actual = repository.findById(memberId);

        // then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("Find Member by email")
    void findByEmail() {
        // given
        String expected = "kookies@google.com";

        // when
        repository.save(savedMember);
        Optional<Member> foundMember = repository.findByEmail(expected);

        // then
        assertTrue(foundMember.isPresent());
        assertThat(foundMember.get().getEmail()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Update Member password")
    void updateMember() {
        // given
        String newEmail = "updated@google.com";
        savedMember.setEmail(newEmail);

        // when
        Member updatedMember = repository.save(savedMember);

        // then
        assertAll(
                () -> assertThat(updatedMember.getEmail()).isEqualTo(savedMember.getEmail()),
                () -> assertThat(updatedMember.getPassword()).isEqualTo(savedMember.getPassword())
        );
    }

    @Test
    @DisplayName("Delete Member")
    void deleteById() {
        // when
        Long memberId = savedMember.getId();
        repository.deleteById(memberId);
        boolean exists = repository.existsById(memberId);

        // then
        assertThat(exists).isFalse();
    }

}
