package gift.product.exception.option;

import org.springframework.http.HttpStatus;

public class ProductOptionNotFoundException extends ProductOptionException {
    private static final String MESSAGE = "[%s]번 상품의 [%s]번 옵션을 찾을 수 없습니다.";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    private ProductOptionNotFoundException(Long productId, Long optionId) {
        super(String.format(MESSAGE, productId.toString(), optionId.toString()), HTTP_STATUS);
    }

    public static ProductOptionNotFoundException of(Long productId, Long optionId) {
        return new ProductOptionNotFoundException(productId, optionId);
    }
}
