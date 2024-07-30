package gift.dto;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.validation.constraints.NotNull;

public class WishDTO {
    @NotNull
    private Long productId;

    public WishDTO() {
    }

    private WishDTO(WishDTOBuilder builder) {
        this.productId = builder.productId;
    }

    public Long getProductId() {
        return productId;
    }

    public static WishDTO from(Wish wish) {
        return new WishDTOBuilder()
            .productId(wish.getProduct().getId())
            .build();
    }

    public static class WishDTOBuilder {
        private Long productId;

        public WishDTOBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public WishDTO build() {
            return new WishDTO(this);
        }
    }

    public Wish toEntity(Member member, Product product) {
        return new Wish.WishBuilder()
            .member(member)
            .product(product)
            .build();
    }
}
