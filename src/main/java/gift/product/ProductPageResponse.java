package gift.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import org.springframework.data.domain.Page;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductPageResponse(
    int page,
    int size,
    long totalElements,
    int totalPage,
    List<ProductResponse> contents
) {

    public static ProductPageResponse from(Page<Product> productPage) {
        List<ProductResponse> productResponseList = productPage.getContent().stream()
            .map(ProductResponse::from)
            .toList();

        return new ProductPageResponse(
            productPage.getNumber(),
            productPage.getSize(),
            productPage.getTotalElements(),
            productPage.getTotalPages(),
            productResponseList
        );
    }
}
