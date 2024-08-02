package gift.dto.wish;

import gift.dto.product.ProductResponse;
import gift.dto.option.OptionResponse;
import gift.entity.Product;
import gift.entity.Wish;

import java.util.List;

public class WishResponse {
    private Long id;
    private ProductResponse productResponse;

    public WishResponse(Wish wish) {
        this.id = wish.getId();

        Product productEntity = wish.getProduct();
        List<OptionResponse> options = productEntity.getOptions().stream().map(option -> new OptionResponse(option.getId(), option.getName(), option.getQuantity())).toList();

        this.productResponse = new ProductResponse(
                productEntity.getId(),
                productEntity.getCategoryId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                options
        );
    }
}
