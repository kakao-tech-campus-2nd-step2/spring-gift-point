package gift.web.validation.validator;

import gift.web.validation.constraints.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private final int MIN_LENGTH = 8;
    private final int MAX_LENGTH = 15;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * 비밀번호가 제약조건에 맞는지 확인한다.<br>
     * - 길이는 8자 이상 15자 이하<br>
     * - 영문자와 숫자를 최소 1자 이상 포함하여야 한다.
     * - 영문자와 숫자 이외의 문자는 허용하지 않는다.<br>
     * @param password 검증 대상
     * @param context 컨텍스트
     *
     * @return 제약조건을 만족하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (isInvalidLength(password)) {
            return false;
        }
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$");
    }

    private boolean isInvalidLength(String password) {
        return password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH;
    }
}
