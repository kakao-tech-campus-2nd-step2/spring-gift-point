package gift.dto.common.valid;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @deprecated Replaced by ProductNameValidator
 */
@Deprecated
public class ProductNameKakaoValidator implements
    ConstraintValidator<ValidProductNameKakao, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        Pattern kakao = Pattern.compile("^(?!.*카카오).*$");
        Matcher kakaoMatcher = kakao.matcher(name);

        return kakaoMatcher.matches();
    }
}

