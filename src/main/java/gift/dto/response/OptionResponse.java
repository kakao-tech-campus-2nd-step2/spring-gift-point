package gift.dto.response;

import gift.entity.Option;

public record OptionResponse(
        Long id,
        String name,
        Integer quantity,
        Long productId
) {
    public static OptionResponse fromOption(Option option) {
        return new OptionResponse(
                option.getId(),
                option.getName(),
                option.getQuantity(),
                option.getProductId()
        );
    }
}
