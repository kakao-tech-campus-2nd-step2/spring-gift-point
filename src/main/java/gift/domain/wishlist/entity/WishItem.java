package gift.domain.wishlist.entity;

import gift.domain.member.entity.Member;
import gift.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "wishlist",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "product_id" }) }
)
public class WishItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected WishItem() {

    }

    public WishItem(Long id, Member member, Product product) {
        this.id = id;
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getProductId() {
        return product.getId();
    }
}
