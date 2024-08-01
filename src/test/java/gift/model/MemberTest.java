package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void testCreateValidMember() {
        Member member = new Member(1L, "kbm", "kbm@kbm.com", "mbk", "user");
        assertAll(
            () -> assertThat(member.getId()).isNotNull(),
            () -> assertThat(member.getName()).isEqualTo("kbm"),
            () -> assertThat(member.getEmail()).isEqualTo("kbm@kbm.com"),
            () -> assertThat(member.getPassword()).isEqualTo("mbk"),
            () -> assertThat(member.getRole()).isEqualTo("user")
        );
    }

    @Test
    void testCreateWithNullName() {
        try {
            Member nullNameMember = new Member(1L, null, "kbm@kbm.com", "mbk", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithEmptyName() {
        try {
            Member emptyNameMember = new Member(1L, "", "kbm@kbm.com", "mbk", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithNullEmail() {
        try {
            Member nullEmailMember = new Member(1L, "kbm", null, "mbk", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithEmptyEmail() {
        try {
            Member emptyEmailMember = new Member(1L, "kbm", "", "mbk", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithInvalidEmail() {
        try {
            Member invalidEmailMember = new Member(1L, "kbm", "kbm", "mbk", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithNullPassword() {
        try {
            Member nullPasswordMember = new Member(1L, "kbm", "kbm@kbm.com", null, "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithEmptyPassword() {
        try {
            Member emptyPasswordMember = new Member(1L, "kbm", "kbm@kbm.com", "", "user");
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }
}