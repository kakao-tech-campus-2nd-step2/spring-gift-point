package gift.validation.validator;

import gift.validation.constraint.NameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<NameConstraint, String> {

    String charLenErrorMsg = " 상품 이름은 공백을 포함하여 최대 15자까지만 가능";
    String specialCharErrorMsg = " ( ), [ ], +, -, &, /, _ 그 외 특수 문자 사용 불가";
    String kakaoErrorMsg = " '카카오''가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능";

    @Override
    public void initialize(NameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nameField, ConstraintValidatorContext cxt) {
        boolean nameLengthInvalid = nameField.length() > 15;
        boolean containSpecialChar = !nameField.matches("^[a-zA-Z가-힣()\\[\\]\\+\\-&/_\s]+$");
        boolean containKakaoChar = nameField.contains("카카오");
        String returnMsg = "";
        if (nameLengthInvalid) {
            returnMsg += charLenErrorMsg;
        }
        if (containSpecialChar) {
            returnMsg += specialCharErrorMsg;
        }
        if (containKakaoChar) {
            returnMsg += kakaoErrorMsg;
        }
        if (!returnMsg.equals("")) return returnValidationResult(returnMsg, cxt);
        return true;
    }

    private static boolean returnValidationResult(String errorMsg, ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate(errorMsg)
                .addConstraintViolation();
        return false;
    }
}


