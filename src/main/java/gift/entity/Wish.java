package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Integer quantity;

    protected Wish() {
    }

    public Wish(Member member, Product product, LocalDateTime localDateTime, Integer quantity) {
        validateQuantity(quantity);

        this.member = member;
        this.product = product;
        this.createdDate = localDateTime;
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

    public LocalDateTime getCreatedDate() { return createdDate; }

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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Wish wish = (Wish) object;
        return Objects.equals(getId(), wish.getId()) && Objects.equals(getMember(),
                wish.getMember()) && Objects.equals(getProduct(), wish.getProduct())
                && Objects.equals(getCreatedDate(), wish.getCreatedDate())
                && Objects.equals(getQuantity(), wish.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMember(), getProduct(), getCreatedDate(), getQuantity());
    }
}