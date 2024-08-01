package gift.domain.Menu;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MenuRequest(
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {

}
