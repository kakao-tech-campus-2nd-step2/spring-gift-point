package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "WISH_TABLE")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WISH_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID")
    private Product product;

    @Column(name = "PRODUCT_COUNT",nullable = false)
    private Integer productCount;

    public Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
        this.productCount = 1;
    }

    public Wish(Long id, Member member, Product product, Integer productCount) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.productCount = productCount;
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

    public void productCountUpdate(Integer productCount) {
        this.productCount = productCount;
    }

    public String getProductName(){
        return product.getName();
    }

    public int getProductCount(){
        return this.productCount;
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
        return member.equals(wish.member) && product.equals(wish.product) && productCount.equals(
            wish.productCount);
    }

    @Override
    public int hashCode() {
        int result = member.hashCode();
        result = 31 * result + product.hashCode();
        result = 31 * result + productCount.hashCode();
        return result;
    }
}
