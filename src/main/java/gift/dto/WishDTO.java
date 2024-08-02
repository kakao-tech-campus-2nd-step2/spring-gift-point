package gift.dto;

import jakarta.validation.constraints.NotNull;

public class WishDTO {

    private Long id;

    @NotNull
    private Long memberId;

    @NotNull
    private Long productId;

    public WishDTO(Long id, Long memberId, Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
