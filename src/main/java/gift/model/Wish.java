package gift.model;

import gift.exception.InputException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "wish",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "constraintName",
            columnNames = {"member_id", "product_id"}
        )
    }
)
public class Wish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_wish_member_id_ref_member_id"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"))
    private Product product;

    protected Wish() {
    }

    public Wish(Member member, Product product) {
        validateMember(member);
        validateProduct(product);
        this.member = member;
        this.product = product;
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

    private void validateMember(Member member) {
        if(member == null || member.getId() == null) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }

    private void validateProduct(Product product) {
        if(product == null || product.getId() == null) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }
}
