package gift.exceptionAdvisor.exceptions;

import org.springframework.http.HttpStatus;

public class ProductNoSuchException extends ProductServiceException {

    public ProductNoSuchException() {
        super("상품을 찾을 수 없습니다", HttpStatus.FORBIDDEN);
    }
}
