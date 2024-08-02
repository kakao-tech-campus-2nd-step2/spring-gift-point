package gift.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.product.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductResponseListDto(@JsonProperty("total_page") int totalPage, @JsonProperty("content") List<Product> products) {
    public static ProductResponseListDto productPageToProductResponseListDto(Page<Product> products) {
        return new ProductResponseListDto(products.getTotalPages(), products.getContent());
    }
}
