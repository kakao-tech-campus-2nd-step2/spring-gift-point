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
            throw new EntityNotFoundException("Product does not exist in " + memberId +"'s wish");
        }
    }

    public void checkWishByProductId(Long productId) {
        if (!containsProduct(productId)) {
            throw new EntityNotFoundException("Product with id " + productId + " does not exist");
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
