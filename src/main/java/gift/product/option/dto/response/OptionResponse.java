package gift.product.option.dto.response;

import gift.product.option.entity.Option;

public record OptionResponse(
    Long id,
    String name,
    Integer quantity,
    Long productId
) {

    public static OptionResponse from(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity(),
            option.getProductId());
    }

}
