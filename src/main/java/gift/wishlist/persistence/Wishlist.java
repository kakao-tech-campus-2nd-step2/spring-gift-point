package gift.wishlist.persistence;

import gift.member.persistence.Member;
import gift.product.persistence.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    private LocalDateTime createdDate;

    private int quantity;

    public Wishlist() {
    }

    public Wishlist(Member member, Product product, int quantity) {
        this.member = member;
        this.product = product;
        this.createdDate = LocalDateTime.now();
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public int getQuantity() {
        return quantity;
    }
}
