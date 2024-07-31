package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wish_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private boolean isDeleted = false;

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Wish() {

    }

    public Long getWishId() {
        return wish_id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setWishId(long wish_id) {
        this.wish_id = wish_id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean b) {
        this.isDeleted = b;
    }
}
