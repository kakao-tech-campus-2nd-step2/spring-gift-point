package gift.dto;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;

public class WishDTO {

    private Long memberId;
    private Long productId;

    public WishDTO() {

    }

    public WishDTO(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Wish toEntity(Member member, Product product) {
        return new Wish(member, product);
    }
}
