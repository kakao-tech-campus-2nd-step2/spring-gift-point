package gift.product.model;

import jakarta.persistence.*;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    public Wish() {}

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
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
}
