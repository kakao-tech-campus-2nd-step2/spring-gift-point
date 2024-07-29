package gift.dto;

import gift.model.Product;
import java.util.List;

public record ProductsPageResponseDTO(List<Product> products,
                                      Integer currentPage,
                                      Integer totalPages) {
}
