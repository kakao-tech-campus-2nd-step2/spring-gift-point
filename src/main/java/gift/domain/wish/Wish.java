package gift.domain.wish;

import gift.domain.member.Member;
import gift.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long count;

    protected Wish() {

    }

    public Wish(Member member, Product product, Long count) {
        this.member = member;
        this.product = product;
        this.count = count;
    }

    public void updateWish(Long count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setMember(Member member) {
        // 기존에 이미 member가 존재한다면, 관계를 끊어줘야함.
        if(this.member != null){
            this.member.getWishList().remove(this);
        }

        this.member = member;
        member.getWishList().add(this);
    }

    public void setProduct(Product product) {
        if(this.product != null){
            this.product.getWishList().remove(this);
        }

        this.product = product;
        product.getWishList().add(this);
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Long getCount() {
        return count;
    }


}
