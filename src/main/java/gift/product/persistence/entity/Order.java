package gift.product.persistence.entity;

import gift.global.domain.BaseTimeEntity;
import gift.member.persistence.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private Long optionId;

    @NotNull
    private Integer quantity;

    private String message;

    protected Order() {
    }

    public Order(Product product, Member member, Long optionId, Integer quantity, String message) {
        this.product = product;
        this.member = member;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getId() {
        return id;
    }
}
