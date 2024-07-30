package gift.domain.option.dto;

import gift.domain.option.Option;

public record SimpleOptionDTO(
    Long id,
    String name,
    Long quantity
) {

    public SimpleOptionDTO(Option option) {
        this(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }
}

