package gift.model;

import java.util.List;

public record ProductPageDTO(int pageNumber, int pageSize, long totalElements, List<ProductDTO> products) {

}
