package gift.dto.wish;

import gift.model.Wish;
import org.springframework.data.domain.Page;

public record WishPageResponseDTO(Page<Wish> wishes) {
}
