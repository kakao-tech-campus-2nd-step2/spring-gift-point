package gift.model;

import static gift.util.constants.MemberConstants.INVALID_EMAIL;
import static gift.util.constants.MemberConstants.INVALID_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Member 모델 생성 테스트")
    public void testCreateMember() {
        Member member = new Member(1L, "test@example.com", "password");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isEmpty();
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.isEmailMatching("test@example.com")).isTrue();
        assertThat(member.isPasswordMatching("password")).isTrue();
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 Member 생성")
    public void testCreateMemberInvalidEmail() {
        Member member = new Member(1L, "invalid-email", "password");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals(INVALID_EMAIL)
        );
    }

    @Test
    @DisplayName("비밀번호가 너무 짧을 때 Member 생성")
    public void testCreateMemberShortPassword() {
        Member member = new Member(1L, "test@example.com", "123");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals(INVALID_PASSWORD)
        );
    }

    @Test
    @DisplayName("Member 모델 업데이트 테스트")
    public void testUpdateMember() {
        Member member = new Member(1L, "test@example.com", "password");
        member.update("new@example.com", "newpassword");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isEmpty();
        assertThat(member.isEmailMatching("new@example.com")).isTrue();
        assertThat(member.isPasswordMatching("newpassword")).isTrue();
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 Member 업데이트")
    public void testUpdateMemberInvalidEmail() {
        Member member = new Member(1L, "test@example.com", "password");
        member.update("invalid-email", "newpassword");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals(INVALID_EMAIL)
        );
    }

    @Test
    @DisplayName("비밀번호가 너무 짧을 때 Member 업데이트")
    public void testUpdateMemberShortPassword() {
        Member member = new Member(1L, "test@example.com", "password");
        member.update("new@example.com", "123");

        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals(INVALID_PASSWORD)
        );
    }

    @Test
    @DisplayName("Member 모델 이메일 매칭 테스트")
    public void testEmailMatching() {
        Member member = new Member(1L, "test@example.com", "password");

        assertThat(member.isEmailMatching("test@example.com")).isTrue();
        assertThat(member.isEmailMatching("wrong@example.com")).isFalse();
    }

    @Test
    @DisplayName("Member 모델 비밀번호 매칭 테스트")
    public void testPasswordMatching() {
        Member member = new Member(1L, "test@example.com", "password");

        assertThat(member.isPasswordMatching("password")).isTrue();
        assertThat(member.isPasswordMatching("wrongpassword")).isFalse();
    }

    @Test
    @DisplayName("Member 모델 ID 매칭 테스트")
    public void testIdMatching() {
        Member member = new Member(1L, "test@example.com", "password");

        assertThat(member.isIdMatching(1L)).isTrue();
        assertThat(member.isIdMatching(2L)).isFalse();
    }
}
