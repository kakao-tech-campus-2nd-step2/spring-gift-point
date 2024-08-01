package gift.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "wishes")
public class Wish extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }


    public Wish() {
    }

    public Wish(Member member, Product product) {
        super();
        this.member = member;
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public static class Builder {
        private Member member;
        private Product product;

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Wish build() {
            return new Wish(member, product);
        }
    }
}
