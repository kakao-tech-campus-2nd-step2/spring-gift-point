package gift.option.dto;

import gift.option.domain.*;
import gift.product.domain.Product;

public record OptionServiceDto(Long id, OptionName name, OptionQuantity count, Long productId) {
    public Option toOption(Product product) {
        return new Option(id, name, count, product);
    }
}
