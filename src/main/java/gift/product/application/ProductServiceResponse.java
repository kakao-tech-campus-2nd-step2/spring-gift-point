package gift.product.application;

import gift.category.application.CategoryServiceResponse;
import gift.option.application.OptionServiceResponse;
import gift.product.domain.Product;

import java.util.List;

public record ProductServiceResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        CategoryServiceResponse categoryServiceResponse,
        List<OptionServiceResponse> optionServiceResponseList
){
    public static ProductServiceResponse from(Product product) {
        return new ProductServiceResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                CategoryServiceResponse.from(product.getCategory()),
                product.getOptions().stream()
                        .map(OptionServiceResponse::from)
                        .toList()
        );
    }
}
