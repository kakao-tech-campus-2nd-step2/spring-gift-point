package gift.domain;

import gift.utils.TimeStamp;
import jakarta.persistence.*;

@Entity
public class Wish extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int count;

    public Wish() {
    }

    private Wish(Builder builder) {
        this.product = builder.product;
        this.member = builder.member;
        this.count = builder.count;
    }

    public static class Builder {
        private Product product;
        private Member member;
        private int count;

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Wish build() {
            return new Wish(this);
        }
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

    public int getCount() {
        return count;
    }

    public void update(int count){
        this.count = count;
    }

    public void addMember(Member member){
        this.member = member;
        member.getWishList().add(this);
    }

    public void addProduct(Product product){
        this.product = product;
        product.getWishList().add(this);
    }

    public void removeMember(){
        member.getWishList().remove(this);
        this.member = null;
    }
}
