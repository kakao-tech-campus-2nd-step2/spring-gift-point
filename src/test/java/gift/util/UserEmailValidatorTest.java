package gift.util;


import gift.entity.UserDTO;
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
        UserDTO user = new UserDTO("test@naver.com", "abc");

        //when
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        //then
        assertThat(violations).isEmpty();
    }

    @Test
    public void save_emailFailure() {
        //given
        UserDTO user = new UserDTO("test@123@naver,com", "abc");

        //when
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        //then
        assertThat(violations).isNotEmpty();
    }
}
