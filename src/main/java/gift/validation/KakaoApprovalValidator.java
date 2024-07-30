package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class KakaoApprovalValidator implements ConstraintValidator<KakaoApproval, String> {

    @Override
    public void initialize(KakaoApproval constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.contains("카카오") || isApprovedByMD();
    }

    private boolean isApprovedByMD() {
        return false;
    }
}
