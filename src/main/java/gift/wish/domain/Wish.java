package gift.wish.domain;

import gift.member.domain.Member;
import gift.product.domain.Product;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "wishes")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // wish list 를 가져올 때, member 는 사용되는 경우가 적으므로, LAZY 로 가져온다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // wish list 를 가져올 때, product 는 거의 무조건 사용되므로, EAGER 로 가져온다
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private ProductCount productCount;

    public Wish() {
    }

    public Wish(Long id, Member member, Product product, ProductCount productCount) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.productCount = productCount;
    }

    public boolean checkNew() {
        return id == null;
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

    public ProductCount getProductCount() {
        return productCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Wish item = (Wish) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void increaseProductCount(ProductCount productCount) {
        this.productCount = new ProductCount(this.productCount.getProductCountValue() + productCount.getProductCountValue());
    }
}
