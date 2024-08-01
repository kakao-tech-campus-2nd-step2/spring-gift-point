package gift.domain;

import gift.domain.base.BaseEntity;
import gift.domain.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WishProduct extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    protected WishProduct() {
    }

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private Member member;
        private Product product;
        private Integer quantity;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public WishProduct build() {
            return new WishProduct(this);
        }
    }

    private WishProduct(Builder builder) {
        super(builder);
        member = builder.member;
        product = builder.product;
        quantity = builder.quantity;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer updateQuantity(Integer quantity) {
        this.quantity = quantity;
        return this.quantity;
    }

    public Integer addQuantity(Integer quantity) {
        this.quantity += quantity;
        return this.quantity;
    }
}
