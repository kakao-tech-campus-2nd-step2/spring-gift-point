package gift.domain.MenuDomain;

import gift.domain.OptionDomain.Option;

import java.util.List;

public record MenuRequest(
        String name,
        int price,
        String imageUrl,
        Long categoryId,
        List<Option> options
) {

}
