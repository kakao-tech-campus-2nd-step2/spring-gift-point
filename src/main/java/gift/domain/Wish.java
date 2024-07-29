package gift.domain;

import jakarta.persistence.*;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_name", referencedColumnName = "name", nullable = false)
    private Product product;

    public Wish() {}

    public Wish(Long id, Product product, Member member) {
        this.id = id;
        this.product = product;
        this.member = member;
    }

    public Wish(Product product, Member member) {
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }


    public Member getMember() {
        return member;
    }

}
