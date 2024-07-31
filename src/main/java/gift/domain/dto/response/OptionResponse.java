package gift.domain.dto.response;

import gift.domain.entity.Option;
import gift.domain.entity.Product;
import java.util.List;

public record OptionResponse(Long id, String name, Integer quantity) {

    public static OptionResponse of(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity());
    }

    public static List<OptionResponse> of(List<Option> options) {
        return options.stream()
            .map(OptionResponse::of)
            .toList();
    }

    public Option toEntity(Product product) {
        return new Option(product, name, quantity);
    }
}
