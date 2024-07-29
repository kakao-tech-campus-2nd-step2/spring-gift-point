package gift.dto;

import gift.model.Wish;
import java.util.List;

public record WishPageResponseDTO(List<Wish> wishes,
                                  Integer currentPage,
                                  Integer totalPages) {
}
