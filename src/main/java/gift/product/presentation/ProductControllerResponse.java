package gift.product.presentation;

import gift.option.application.OptionServiceResponse;
import gift.product.application.ProductServiceResponse;

import java.util.List;

public record ProductControllerResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        List<OptionServiceResponse> optionServiceResponseList
){
    public static ProductControllerResponse from(ProductServiceResponse product) {
        return new ProductControllerResponse(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl(),
                product.categoryId(),
                product.optionServiceResponseList()
        );
    }
}
