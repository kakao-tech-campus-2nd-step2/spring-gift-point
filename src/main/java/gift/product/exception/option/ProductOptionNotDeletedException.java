package gift.product.exception.option;

import org.springframework.http.HttpStatus;

public class ProductOptionNotDeletedException extends ProductOptionException {
    private static final String MESSAGE = "[%s]번 상품의 [%s]번 옵션을 삭제할 수 없습니다.";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    private ProductOptionNotDeletedException(Long productId, Long optionId) {
        super(String.format(MESSAGE, productId.toString(), optionId.toString()), HTTP_STATUS);
    }

    public static ProductOptionNotDeletedException of(Long productId, Long optionId) {
        return new ProductOptionNotDeletedException(productId, optionId);
    }
}
