package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.InvalidInputValueException;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void testCreateValidMember() {
        Member member = new Member(1L, "kbm@kbm.com", "mbk", "user", 0L);
        assertAll(
            () -> assertThat(member.getId()).isNotNull(),
            () -> assertThat(member.getEmail()).isEqualTo("kbm@kbm.com"),
            () -> assertThat(member.getPassword()).isEqualTo("mbk"),
            () -> assertThat(member.getRole()).isEqualTo("user")
        );
    }

    @Test
    void testCreateWithNullEmail() {
        try {
            Member nullEmailMember = new Member(1L, null, "mbk", "user", 0L);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyEmail() {
        try {
            Member emptyEmailMember = new Member(1L, "", "mbk", "user", 0L);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithInvalidEmail() {
        try {
            Member invalidEmailMember = new Member(1L, "kbm", "mbk", "user", 0L);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithNullPassword() {
        try {
            Member nullPasswordMember = new Member(1L, "kbm@kbm.com", null, "user", 0L);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyPassword() {
        try {
            Member emptyPasswordMember = new Member(1L, "kbm@kbm.com", "", "user", 0L);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }
}