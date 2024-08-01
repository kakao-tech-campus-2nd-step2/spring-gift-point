package gift.domain;

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
