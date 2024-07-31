package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    protected Wish() {
    }

    public Wish(Member member, Product product, Option option) {
        this.member = member;
        this.product = product;
        this.option = option;
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

    public Option getOption() {
        return option;
    }
}