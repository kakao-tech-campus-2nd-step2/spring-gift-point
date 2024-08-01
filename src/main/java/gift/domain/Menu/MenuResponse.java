package gift.domain.Menu;

import gift.domain.Category.Category;
import gift.domain.Option.Option;

import java.util.Set;

public record MenuResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        Category category,
        Set<Option> options
) {

}
