package gift.domain.product.dto;

import gift.domain.option.dto.OptionResponse;

public record ProductCreateResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId,
    OptionResponse optionResponse
) {

    public ProductCreateResponse(ProductResponse response, OptionResponse optionResponse) {
        this(
            response.id(),
            response.name(),
            response.price(),
            response.imageUrl(),
            response.categoryId(),
            optionResponse
        );
    }
}
