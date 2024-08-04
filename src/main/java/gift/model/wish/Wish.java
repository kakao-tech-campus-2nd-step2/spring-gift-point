package gift.model.wish;

import gift.model.BaseTimeEntity;
import gift.model.member.Member;
import gift.model.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Wish extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    @NotNull
    private Long count;

    protected Wish() {
    }

    public Wish(Long id, Member member, Product product, Long count) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.count = count;
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

    public Long getCount() {
        return count;
    }

    //== 비즈니스 로직 ==//
    public void updateCount(Long count) {
        this.count = count;
    }

    public boolean isOwner(Long memberId) {
        return member.getId().equals(memberId);
    }
}
