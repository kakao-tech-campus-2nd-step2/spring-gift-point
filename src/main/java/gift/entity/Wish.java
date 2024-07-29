package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "wish")
public class Wish {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Wish() {
    }

    public Wish(Product product, Member member) {
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }

}
