package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "wish")
@Schema(description = "위시")
public class Wish {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "위시의 고유 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "위시리스트에 들어간 상품 정보")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @Schema(description = "해당 위시를 소유하고있는 멤버 정보")
    private Member member;

    public Wish() {
    }

    public Wish(Product product, Member member) {
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Product getProduct() {
        return product;
    }

    public Member getMember() {
        return member;
    }

}
