package gift.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 입력 값이 유효한 케이스")
    void testMemberSuccess() {
        MemberDTO memberDTO = new MemberDTO(1L, "admin@email.com", "password");
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("올바르지 않은 이메일")
    void testInvalidEmail() {
        MemberDTO memberDTO = new MemberDTO(1L, "invalid_email", "password");
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 이메일 형식으로 입력해 주세요.");
    }

    @Test
    @DisplayName("비어있는 패스워드")
    void testNullPassword() {
        MemberDTO memberDTO = new MemberDTO(1L, "admin@email.com", "");
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(memberDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("비밀번호는 필수 항목입니다.");
    }

}
