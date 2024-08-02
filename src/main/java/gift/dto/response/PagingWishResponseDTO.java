package gift.dto.response;

import java.util.List;

public record PagingWishResponseDTO(List<WishResponseDTO> content,
                                    int number,
                                    int totalElements,
                                    int size,
                                    boolean last) {
}
