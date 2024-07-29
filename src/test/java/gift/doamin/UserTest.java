package gift.doamin;

import gift.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void userTest(){
        User user = new User("Bae", "Bae@email.com", "aaaa");
        Assertions.assertThat((user)).isNotNull();

        user.updateEntity("Bae1@gamil.com", "aaaaa");
        Assertions.assertThat(user.getUserId()).isEqualTo("Bae");
        Assertions.assertThat(user.getEmail()).isEqualTo("Bae1@gamil.com");
        Assertions.assertThat(user.getPassword()).isEqualTo("aaaaa");
    }
}
