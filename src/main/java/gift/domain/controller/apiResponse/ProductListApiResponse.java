package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.domain.dto.response.ProductWithCategoryIdResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class ProductListApiResponse extends BasicApiResponse {

    private final List<CategoryResponse> categories;
    private final List<ProductWithCategoryIdResponse> products;

    public ProductListApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode status,
        @JsonProperty(value = "categories", required = true) List<CategoryResponse> categories,
        @JsonProperty(value = "products", required = true) List<ProductWithCategoryIdResponse> products
    ) {
        super(status);
        this.categories = categories;
        this.products = products;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }

    public List<ProductWithCategoryIdResponse> getProducts() {
        return products;
    }
}
