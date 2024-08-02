package gift.domain;

import gift.dto.wishedProduct.GetWishedProductResponse;
import gift.dto.wishedProduct.WishedProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "wished_product", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_email", "product_id"})
})
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

    protected WishedProduct() {

    }

    public WishedProduct(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public WishedProductDto toDto() {
        return new WishedProductDto(id, member.getEmail(), product.getId());
    }

    public GetWishedProductResponse toGetWishedProductResponse() {
        return new GetWishedProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), id);
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
}
