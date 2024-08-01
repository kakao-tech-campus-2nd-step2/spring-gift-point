package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.domain.dto.response.ProductCoreInfoResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class ProductListApiResponse extends BasicApiResponse {

    private final CategoryResponse category;
    private final List<ProductCoreInfoResponse> products;

    public ProductListApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode status,
        @JsonProperty(value = "category", required = true) CategoryResponse category,
        @JsonProperty(value = "products", required = true) List<ProductCoreInfoResponse> products
    ) {
        super(status);
        this.category = category;
        this.products = products;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public List<ProductCoreInfoResponse> getProducts() {
        return products;
    }
}
