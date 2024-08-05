package gift.product.presentation.response;

import gift.category.application.CategoryServiceResponse;
import gift.option.application.OptionService;
import gift.option.application.OptionServiceResponse;
import gift.product.application.ProductServiceResponse;

import java.util.List;

public record ProductReadResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        String categoryName
){
    public static ProductReadResponse from(ProductServiceResponse product) {
        return new ProductReadResponse(
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl(),
                product.categoryServiceResponse().id(),
                product.categoryServiceResponse().name()
        );
    }
}
