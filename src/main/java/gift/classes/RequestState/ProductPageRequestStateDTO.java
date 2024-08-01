package gift.classes.RequestState;

import gift.dto.ProductPageDto;
import org.springframework.http.HttpStatus;

public class ProductPageRequestStateDTO extends RequestStateDTO {

    private final ProductPageDto data;

    public ProductPageRequestStateDTO(HttpStatus status, String message,
        ProductPageDto data) {
        super(status, message);
        this.data = data;
    }

    public ProductPageDto getData() {
        return data;
    }
}
