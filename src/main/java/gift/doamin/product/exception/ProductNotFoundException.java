package gift.doamin.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotFoundException extends ResponseStatusException {

    public ProductNotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다");
    }
}
