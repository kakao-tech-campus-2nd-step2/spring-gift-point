package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.ProductResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class ProductAddApiResponse extends BasicApiResponse {

    @JsonProperty(value = "created-product")
    private final ProductResponse createdProduct;

    public ProductAddApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "created-product", required = true) ProductResponse createdProduct
    ) {
        super(statusCode);
        this.createdProduct = createdProduct;
    }

    public ProductResponse getCreatedProduct() {
        return createdProduct;
    }
}
