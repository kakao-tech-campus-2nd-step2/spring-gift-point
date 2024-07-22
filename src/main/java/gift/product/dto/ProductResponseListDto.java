package gift.product.dto;

import gift.product.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductResponseListDto(int page, List<Product> products) {
    public static ProductResponseListDto productPageToProductResponseListDto(Page<Product> products) {
        return new ProductResponseListDto(products.getTotalPages(), products.getContent());
    }
}
