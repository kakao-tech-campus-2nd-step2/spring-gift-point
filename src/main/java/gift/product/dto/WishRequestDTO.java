package gift.product.dto;

import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import jakarta.validation.constraints.NotNull;

public class WishRequestDTO {

    @NotNull
    private Long productId;

    public WishRequestDTO() {

    }

    public WishRequestDTO(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public Wish convertToDomain(Member member, Product product) {
        return new Wish(
            member,
            product
        );
    }
}
