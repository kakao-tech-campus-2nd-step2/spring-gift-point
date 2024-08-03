package gift.entity;

import gift.entity.user.UserDTO;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDTOTest {

    private UserDTO user;

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        user = new UserDTO("test@naver.com", "test");
    }

    @Test
    void 이메일_형식이_아닌_경우_테스트() {
        // given
        user.setEmail("te@st@@na.ver,com");

        // when
        var validate = validator.validate(user);

        // then
        assertThat(validate).isNotEmpty();
    }

    @Test
    void 이메일_형식이_맞는_경우_테스트() {
        // given
        user.setEmail("test@naver.com");

        // when
        var validate = validator.validate(user);

        // then
        assertThat(validate).isEmpty();
    }
}
