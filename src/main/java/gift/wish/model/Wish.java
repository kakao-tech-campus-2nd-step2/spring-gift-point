package gift.wish.model;

import gift.common.exception.WishException;
import gift.common.model.BaseEntity;
import gift.member.model.Member;
import gift.product.model.Product;
import gift.wish.WishErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Wish extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public void validateMember(Long memberId) throws WishException {
        if (!member.getId().equals(memberId)) {
            throw new WishException(WishErrorCode.NOT_FOUND);
        }
    }
}
