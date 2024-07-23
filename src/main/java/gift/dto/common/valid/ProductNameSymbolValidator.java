package gift.dto.common.valid;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @deprecated Replaced by ProductNameValidator
 */
@Deprecated
public class ProductNameSymbolValidator implements
    ConstraintValidator<ValidProductNameSymbol, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {

        Pattern symbol = Pattern.compile("^[\\w가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-&/_]*$");
        Matcher symbolMatcher = symbol.matcher(name);

        return symbolMatcher.matches();
    }
}


