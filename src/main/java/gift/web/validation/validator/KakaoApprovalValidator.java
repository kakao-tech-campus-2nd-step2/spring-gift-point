package gift.web.validation.validator;

import gift.utils.StringUtils;
import gift.web.validation.constraints.RequiredKakaoApproval;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class KakaoApprovalValidator implements ConstraintValidator<RequiredKakaoApproval, String> {

    private Set<String> requiredKakaoApprovalNames;

    @Override
    public void initialize(RequiredKakaoApproval constraintAnnotation) {
        requiredKakaoApprovalNames = Set.of("카카오", "kakao");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringUtils.containsAnySubstring(value.toLowerCase(), requiredKakaoApprovalNames);
    }
}
