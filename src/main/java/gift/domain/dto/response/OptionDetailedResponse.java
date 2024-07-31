package gift.domain.dto.response;

import gift.domain.entity.Option;
import gift.domain.service.ProductService;
import java.util.List;

public record OptionDetailedResponse(Long id, Long productId, String name, Integer quantity) {

    public static OptionDetailedResponse of(Option option) {
        return new OptionDetailedResponse(option.getId(), option.getProduct().getId(), option.getName(), option.getQuantity());
    }

    public static List<OptionDetailedResponse> of(List<Option> options) {
        return options.stream()
            .map(OptionDetailedResponse::of)
            .toList();
    }

    public Option toEntity(ProductService productService) {
        return new Option(productService.getProductById(productId), name, quantity);
    }
}
