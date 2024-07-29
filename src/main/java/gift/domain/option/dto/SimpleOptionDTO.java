package gift.domain.option.dto;

import gift.domain.option.Option;

public record SimpleOptionDTO( // 상품 정보 제외
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

