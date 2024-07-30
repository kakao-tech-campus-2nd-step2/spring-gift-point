package gift.controller.dto.response;

import gift.model.Product;

import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    public record Info(
            Long productId,
            String name,
            int price,
            String imageUrl
    ) {
        public static Info from(Product product) {
            return new Info(
                    product.getId(), product.getName(), product.getPrice(),
                    product.getImageUrl());
        }
    }

    public record WithOption(
            Long id, String name, int price,
            String imageUrl,
            List<OptionResponse> options,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        public static WithOption from(Product product) {
            return new WithOption(
                    product.getId(), product.getName(), product.getPrice(),
                    product.getImageUrl(),
                    product.getOptions().stream().map(OptionResponse::from).toList(),
                    product.getCreatedAt(), product.getUpdatedAt());
        }
    }
}
