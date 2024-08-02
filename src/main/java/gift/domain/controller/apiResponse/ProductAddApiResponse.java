package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.ProductWithCategoryIdResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class ProductAddApiResponse extends BasicApiResponse {

    @JsonProperty(value = "product") private final ProductWithCategoryIdResponse product;

    public ProductAddApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "product", required = true) ProductWithCategoryIdResponse product
    ) {
        super(statusCode);
        this.product = product;
    }

    public ProductWithCategoryIdResponse getProduct() {
        return product;
    }
}
