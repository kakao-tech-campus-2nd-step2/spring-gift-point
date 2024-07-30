package gift.product;

import gift.option.OptionResponse;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId,
    List<OptionResponse> options
) {
    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(),
            product.getCategoryId(), product.getOptionResponses());
    }
}
