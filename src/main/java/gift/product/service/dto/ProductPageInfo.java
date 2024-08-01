package gift.product.service.dto;

import gift.product.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record ProductPageInfo(
        List<ProductInfo> products,
        Pageable pageable,
        Integer totalPages,
        Long totalElements,
        Boolean last,
        Integer number,
        Integer size,
        Integer numberOfElements,
        Boolean first,
        Boolean empty
) {
    public static ProductPageInfo from(Page<Product> productPage) {
        var products = productPage.getContent().stream()
                .map(ProductInfo::from)
                .toList();
        return new ProductPageInfo(
                products,
                productPage.getPageable(),
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.isLast(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getNumberOfElements(),
                productPage.isFirst(),
                productPage.isEmpty()
        );
    }
}
