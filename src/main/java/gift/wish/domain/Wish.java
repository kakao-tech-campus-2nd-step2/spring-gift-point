package gift.wish.domain;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.wish.exception.WishCanNotModifyException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SoftDelete;

@Entity
@SoftDelete
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Wish() {
    }

    public Wish(Long id, Integer amount, Product product, Member member) {
        this.id = id;
        this.amount = amount;
        this.product = product;
        this.member = member;
    }

    public Wish(Integer amount, Product product, Member member) {
        this(null, amount, product, member);
    }

    public Long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public Product getProduct() {
        return product;
    }

    public void modify(final int amount, final Product product, final Member member) {
        checkOwner(member);
        checkProduct(product);
        this.amount = amount;
    }

    public boolean beDeleted() {
        return amount.equals(0);
    }

    private void checkOwner(final Member member) {
        if (!this.member.getId()
                .equals(member.getId())) {
            throw new WishCanNotModifyException();
        }
    }

    private void checkProduct(final Product product) {
        if (!this.product.getId()
                .equals(product.getId())) {
            throw new WishCanNotModifyException();
        }
    }

    public boolean isOwner(final Long userId) {
        return this.member.getId().equals(userId);
    }
}
