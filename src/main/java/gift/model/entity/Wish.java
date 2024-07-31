package gift.model.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_id"), nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_id"), nullable = false)
    private Product product;

    public Wish(long id, Member member, Product product) {
        this.id = id;
        this.member = member;
        this.product = product;
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    protected Wish() {
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Objects.equals(member, wish.member) && Objects.equals(product, wish.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, product);
    }
}
