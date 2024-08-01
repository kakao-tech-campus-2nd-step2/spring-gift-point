package gift.domain.Option;

import gift.domain.Menu.Menu;

public record OptionResponse(
        Long id,
        String name,
        Long quantity,
        Menu menu
) {
}
