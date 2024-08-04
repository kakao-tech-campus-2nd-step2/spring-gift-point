package gift.product.application;

import gift.option.application.OptionServiceResponse;
import gift.product.domain.Product;

import java.util.List;

public record ProductServiceResponse(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        List<OptionServiceResponse> optionServiceResponseList
){
    public static ProductServiceResponse from(Product product) {
        return new ProductServiceResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getOptions().stream()
                        .map(OptionServiceResponse::from)
                        .toList()
        );
    }
}
