package gift.domain.wish;

import gift.domain.BaseTimeEntity;
import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.dto.WishResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Wish extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int count = 1;

    protected Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Wish{" +
               "id=" + id +
               ", member=" + member +
               ", product=" + product +
               '}';
    }

    public void addOneMore() {
        this.count += 1;
    }

    public void updateCount(int count) {
        this.count = count;
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

        return count == wish.count &&
               Objects.equals(id, wish.id) &&
               Objects.equals(member, wish.member) &&
               Objects.equals(product.getId(), wish.product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, product, count);
    }

    public WishResponse toWishResponse() {
        return new WishResponse(
            id,
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
