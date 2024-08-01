package gift.domain.Menu;

import gift.domain.Option.Option;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MenuRequest(
        String name,
        int price,
        String imageUrl,
        Long categoryId,
        List<Option> options
) {

}
