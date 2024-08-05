package gift.product.presentation.response;

import gift.option.application.OptionServiceResponse;

public record ProductReadOptionResponse(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductReadOptionResponse from(OptionServiceResponse option) {
        return new ProductReadOptionResponse(
                option.id(),
                option.name(),
                option.quantity()
        );
    }
}
