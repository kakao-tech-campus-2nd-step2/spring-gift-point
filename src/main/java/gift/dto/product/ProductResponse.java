package gift.dto.product;

import gift.model.product.Product;

public class ProductResponse {
    public record Info(
            Long productId,
            String name,
            int price,
            String imageUrl
    ) {
        public static Info fromEntity(Product product) {
            return new Info(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl());
        }
    }

}
