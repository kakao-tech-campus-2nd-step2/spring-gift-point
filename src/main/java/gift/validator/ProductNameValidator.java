package gift.validator;

import gift.entity.Product;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductNameValidator implements Validator {

    private static final Pattern ALLOWED_PATTERN = Pattern.compile(
        "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_가-힣]*$");
    private static final String DISALLOWED_WORD = "카카오";

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        String name = product.getName();
        if (name == null || name.length() > 15) {
            errors.rejectValue("name", "name.size", "상품의 이름은 최대 15자까지 입력할 수 있습니다.");
        }

        if (!ALLOWED_PATTERN.matcher(name).matches()) {
            errors.rejectValue("name", "name.invalid", "상품의 이름에 사용 불가한 문자가 포함되어 있습니다.");
        }

        if (name.contains(DISALLOWED_WORD)) {
            errors.rejectValue("name", "name.disallowed",
                "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }
    }
}
