package gift.dto.response;

import java.util.List;

public record PagingOrderResponseDTO(List<OrderResponseDTO> content,
                                     int number,
                                     int totalElements,
                                     int size,
                                     boolean last) {
}
