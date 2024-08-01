package gift.dto;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WishDTO {
    @NotNull
    private Long productId;
    private ProductDTO product;
    private String createdDate;

    public WishDTO() {
    }

    private WishDTO(WishDTOBuilder builder) {
        this.productId = builder.productId;
        this.product = builder.product;
        this.createdDate = builder.createdDate;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public static WishDTO from(Wish wish) {
        return new WishDTOBuilder()
            .productId(wish.getProduct().getId())
            .product(ProductDTO.from(wish.getProduct()))
            .createdDate(wish.getCreatedDate().toString())
            .build();
    }

    public static class WishDTOBuilder {
        private Long productId;
        private ProductDTO product;
        private String createdDate;

        public WishDTOBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public WishDTOBuilder product(ProductDTO product) {
            this.product = product;
            return this;
        }

        public WishDTOBuilder createdDate(String createdDate) {
            this.createdDate = createdDate;
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
            .createdDate(LocalDateTime.now())  // 현재 시간을 설정합니다.
            .build();
    }
}
