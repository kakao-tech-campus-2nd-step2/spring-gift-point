package gift.domain;

import gift.dto.WishedProductDTO;
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
@Table(name = "wished_product")
public class WishedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "amount", nullable = false)
    private int amount;

    protected WishedProduct() {

    }

    public WishedProduct(Member member, Product product, int amount) {
        this.member = member;
        this.product = product;
        this.amount = amount;
    }

    public WishedProductDTO toDTO() {
        return new WishedProductDTO(id, member.getEmail(), product.getId(), amount);
    }

    public Long getId() {
        return id;
    }

    public String getMemberEmail() {
        return member.getEmail();
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
