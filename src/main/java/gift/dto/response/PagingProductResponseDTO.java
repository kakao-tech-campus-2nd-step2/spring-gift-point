package gift.dto.response;

import java.util.List;

public record PagingProductResponseDTO(List<ProductResponseDTO> content,
                                       int number,
                                       int totalElements,
                                       int size,
                                       boolean last) {
}
