package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.main.entity.WishProduct;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WishProductResponse(Long id,
                                  long product_id,
                                  String productName,
                                  int productPrice,
                                  String imageUrl) {

    public WishProductResponse(WishProduct wishProduct) {
        this(
                wishProduct.getId(),
                wishProduct.getProduct().getId(),
                wishProduct.product.getName(),
                wishProduct.product.getPrice(),
                wishProduct.getProduct().getImageUrl()
        );

    }

}
