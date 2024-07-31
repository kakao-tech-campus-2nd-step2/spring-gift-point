package gift.product.exception.option;

import org.springframework.http.HttpStatus;

public class ProductOptionDuplicatedException extends ProductOptionException {
    private static final String MESSAGE = "[%s]번 상품에 이미 [%s] 옵션이 포함되어있습니다..";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    private ProductOptionDuplicatedException(Long productId, String optionName) {
        super(String.format(MESSAGE, productId.toString(), optionName), HTTP_STATUS);
    }

    public static ProductOptionDuplicatedException of(Long productId, String optionName) {
        return new ProductOptionDuplicatedException(productId, optionName);
    }
}