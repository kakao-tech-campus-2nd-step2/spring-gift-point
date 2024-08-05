package gift.entity;

import jakarta.persistence.*;


@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "message", nullable = false)
    private String message;

    public OrderItem() {

    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMessage() {
        return message;
    }
}