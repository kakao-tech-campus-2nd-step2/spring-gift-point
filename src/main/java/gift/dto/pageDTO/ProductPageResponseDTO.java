package gift.dto.pageDTO;

import gift.model.Product;
import org.springframework.data.domain.Page;

public record ProductPageResponseDTO(
    Page<Product> products
) {

}
