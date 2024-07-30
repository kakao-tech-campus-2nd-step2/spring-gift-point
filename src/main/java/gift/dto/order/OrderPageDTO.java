package gift.dto.order;

import java.util.List;

public record OrderPageDTO (List<OrderResponseDTO> content, int number, int totalElements, int size, boolean last){

}
