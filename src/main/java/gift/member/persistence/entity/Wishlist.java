package gift.member.persistence.entity;

import gift.global.domain.BaseTimeEntity;
import gift.product.persistence.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlist",
    indexes = {@Index(name = "idx_modified_date", columnList = "modified_date")}
)
public class Wishlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer count;

    public Wishlist(Product product, Member member, Integer count) {
        this.product = product;
        this.member = member;
        this.count = count;
    }

    protected Wishlist() {}

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }
}
