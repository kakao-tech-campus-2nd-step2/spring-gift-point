package gift.product.persistence.entity;

import gift.global.domain.BaseTimeEntity;
import gift.member.persistence.entity.Member;
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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orders",
    indexes = {@Index(name = "idx_orders_created_date", columnList = "created_date")}
)
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

    private Boolean hasCashReceipt;

    private String cashReceiptType;

    private String cashReceiptNumber;

    private String message;

    private Long point;

    protected Order() {
    }

    public Order(Product product, Member member, Long optionId, Integer quantity, Boolean hasCashReceipt, String cashReceiptType, String cashReceiptNumber, String message, Long point) {
        this.product = product;
        this.member = member;
        this.optionId = optionId;
        this.quantity = quantity;
        this.hasCashReceipt = hasCashReceipt;
        this.cashReceiptType = cashReceiptType;
        this.cashReceiptNumber = cashReceiptNumber;
        this.message = message;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOptionId() {
        return optionId;
    }
}
