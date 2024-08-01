package gift.dto.product;

import gift.model.Product;
import org.springframework.data.domain.Page;

public record ProductsPageResponseDTO(Page<Product> products){
}
