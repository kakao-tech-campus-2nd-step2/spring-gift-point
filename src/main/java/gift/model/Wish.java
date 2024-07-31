package gift.model;

import gift.common.exception.EntityNotFoundException;
import jakarta.persistence.*;

@Entity
@Table(indexes = @Index(name = "idx_wish_created_at", columnList = "created_at"))
public class Wish extends BasicEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int productCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wish() {}

    public Wish(Member member, int productCount, Product product) {
        super();
        this.member = member;
        this.productCount = productCount;
        this.product = product;
    }

    public void updateWish(Member member, int productCount, Product product) {
        this.member = member;
        this.productCount = productCount;
        this.product = product;
    }

    public void checkWishByMemberId(Long memberId) {
        if (!isOwner(memberId)) {
            throw new EntityNotFoundException("본인의 위시리스트만 삭제 가능합니다.");
        }
    }

    public void checkWishByProductId(Long productId) {
        if (!containsProduct(productId)) {
            throw new EntityNotFoundException("위시리스트의 상품과 일치하지 않습니다.");
        }
    }

    public boolean isOwner(Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean containsProduct(Long productId) {
        return product.getId().equals(productId);
    }

    public Member getMember() {
        return member;
    }

    public int getProductCount() {
        return productCount;
    }

    public Product getProduct() {
        return product;
    }
}
