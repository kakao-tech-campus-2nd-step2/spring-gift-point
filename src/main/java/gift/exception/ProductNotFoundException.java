package gift.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends ProductException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "Can't find Product with id %s";

    private ProductNotFoundException(final Long id) {
        super(String.format(MESSAGE, id.toString()), HTTP_STATUS);
    }

    public static ProductNotFoundException of(final Long id) {
        return new ProductNotFoundException(id);
    }

}