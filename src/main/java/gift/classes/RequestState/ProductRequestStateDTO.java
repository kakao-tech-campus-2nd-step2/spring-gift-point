package gift.classes.RequestState;

import gift.dto.ProductDto;
import org.springframework.http.HttpStatus;

public class ProductRequestStateDTO extends RequestStateDTO {

    private final ProductDto data;

    public ProductRequestStateDTO(HttpStatus status, String message, ProductDto data) {
        super(status, message);
        this.data = data;
    }

    public ProductDto getData() {
        return data;
    }
}
