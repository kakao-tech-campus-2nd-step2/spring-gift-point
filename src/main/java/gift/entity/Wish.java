package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    protected Wish() {
    }

    public Wish(Member member, Product product, Integer quantity) {
        validateQuantity(quantity);

        this.member = member;
        this.product = product;
        this.quantity = quantity;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void incrementQuantity() { this.quantity++; }

    public void changeQuantity(Integer quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(Integer quantity) {
        if(quantity == null || quantity <= 0)
            throw new BadRequestException("상품의 개수는 0보다 커야합니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id) && Objects.equals(member, wish.member)
                && Objects.equals(product, wish.product) && Objects.equals(quantity,
                wish.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, product, quantity);
    }
}