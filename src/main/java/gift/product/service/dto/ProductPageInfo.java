package gift.product.service.dto;

import gift.product.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public record ProductPageInfo(
        List<ProductInfo> products,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public static ProductPageInfo from(Page<Product> productPage) {
        var products = productPage.getContent().stream()
                .map(ProductInfo::from)
                .toList();

        return new ProductPageInfo(
                products,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber() + 1
        );
    }
}
