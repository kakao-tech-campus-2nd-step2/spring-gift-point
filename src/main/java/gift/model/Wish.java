package gift.model;

import jakarta.persistence.*;

@Entity(name = "wishes")
public class Wish {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Wish(Long id, Product product, Member member) {
        this.id = id;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }
}
