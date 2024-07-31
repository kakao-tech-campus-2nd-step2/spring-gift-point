package gift.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    void memberConstructorAndGetters() {
        Member member = new Member("test@example.com", "password");

        assertThat(member.getEmail()).isEqualTo("test@example.com");
        assertThat(member.getPassword()).isEqualTo("password");
    }

    @Test
    void memberSetters() {
        Member member = new Member();
        member.setEmail("new@example.com");
        member.setPassword("newpassword");

        assertThat(member.getEmail()).isEqualTo("new@example.com");
        assertThat(member.getPassword()).isEqualTo("newpassword");
    }
}
