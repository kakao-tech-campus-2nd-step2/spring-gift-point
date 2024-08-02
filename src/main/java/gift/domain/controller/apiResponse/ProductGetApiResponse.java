package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.ProductWithCategoryIdResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class ProductGetApiResponse extends BasicApiResponse {

    private final ProductWithCategoryIdResponse product;

    public ProductGetApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode status,
        @JsonProperty(value = "product", required = true) ProductWithCategoryIdResponse product
    ) {
        super(status);
        this.product = product;
    }

    public ProductWithCategoryIdResponse getProducts() {
        return product;
    }
}
