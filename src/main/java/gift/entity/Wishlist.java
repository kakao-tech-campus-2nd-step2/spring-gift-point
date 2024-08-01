package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wishlist extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false, referencedColumnName = "email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", nullable = false)
    private Product product;

    protected Wishlist() {
    }

    public Wishlist(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
}
