package gift.category.dto;

import gift.category.domain.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductResponseListDto(int page, List<Category> categories) {
    public static ProductResponseListDto productPageToProductResponseListDto(Page<Category> products) {
        return new ProductResponseListDto(products.getTotalPages(), products.getContent());
    }
}
