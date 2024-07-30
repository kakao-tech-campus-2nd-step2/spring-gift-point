package gift.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wish_member_id_ref_member_id"))
    private Member member;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"))
    private Product product;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdTime;

    public Wish(Member member, Integer quantity, Product product) {
        this.member = member;
        this.quantity = quantity;
        this.product = product;
    }

    protected Wish() {
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void updateQuantity(Integer amount) {
        this.quantity = amount;
    }
}
