package gift.util;

import gift.dto.user.UserRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserEmailValidatorTest {

    @Autowired
    private Validator validator;

    @Test
    public void save_emailSuccess() {
        //given
        UserRequestDTO user = new UserRequestDTO("test@naver.com", "abc");

        //when
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(user);

        //then
        assertThat(violations).isEmpty();
    }

    @Test
    public void save_emailFailure() {
        //given
        UserRequestDTO user = new UserRequestDTO("test@123@naver,com", "abc");

        //when
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(user);

        //then
        assertThat(violations).isNotEmpty();
    }
}
