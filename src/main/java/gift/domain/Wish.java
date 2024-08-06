package gift.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID wishId;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    public Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public UUID getWishId() {
        return wishId;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

}
