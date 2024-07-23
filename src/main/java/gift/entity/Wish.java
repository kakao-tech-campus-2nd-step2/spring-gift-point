package gift.entity;

import jakarta.persistence.*;

@Entity
public class Wish {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Wish(Builder builder) {
        this.member = builder.member;
        this.product = builder.product;
        this.id = builder.id;
    }

    public Wish() {}

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Long getMemberId() {
        return member != null ? member.getId() : null;
    }

    public Long getProductId() {
        return product != null ? product.getId() : null;
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
