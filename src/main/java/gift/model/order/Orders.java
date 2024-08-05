package gift.model.order;

import gift.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long optionId;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer totalPrice;

    protected Orders() {
    }

    public Orders(Long productId, Long memberId, Long optionId, Integer quantity,
        Integer totalPrice) {
        this.productId = productId;
        this.memberId = memberId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    public Long getId() {
        return id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }
}
