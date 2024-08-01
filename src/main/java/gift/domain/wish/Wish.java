package gift.domain.wish;

import gift.domain.member.Member;
import gift.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;

    public Wish(Member member, Product product, Integer quantity) {
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    protected Wish() {
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

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }
}
