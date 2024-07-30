package gift.dto.wish;

import java.util.List;

public record WishPageDTO(List<ResponseWishDTO> content, int number, int totalElements, int size, boolean last) {
}
