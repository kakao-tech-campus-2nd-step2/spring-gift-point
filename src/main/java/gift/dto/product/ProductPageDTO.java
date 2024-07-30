package gift.dto.product;

import java.util.List;

public record ProductPageDTO(List<ResponseProductDTO> content, int number, Long totalElements, int size, boolean last) {
}
