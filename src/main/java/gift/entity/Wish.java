package gift.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    private int quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private Wish(Builder builder) {
        this.member = builder.member;
        this.product = builder.product;
        this.id = builder.id;
    }

    public Wish() {}

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
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

    public Long getProductId() {
        return product != null ? product.getId() : null;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void update(int quantity){
        this.quantity = quantity;
    }

    public static class Builder {
        private Long id;
        private Member member;
        private Product product;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder member(Member member) {
            this.member = member;
            return this;
        }
        public Builder product(Product product) {
            this.product = product;
            return this;
        }
        public Wish build() {
            return new Wish(this);
        }
    }
}
