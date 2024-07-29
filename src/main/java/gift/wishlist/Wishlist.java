package gift.wishlist;

import gift.member.Member;
import gift.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "member_email", nullable = false)
    @ManyToOne
    private Member member;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    protected Wishlist() {
    }

    public Wishlist(Product product, Member member) {
        this.product = product;
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wishlist wishlist) {
            return id == wishlist.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }
}
