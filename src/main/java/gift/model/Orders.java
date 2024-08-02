package gift.model;

import jakarta.persistence.Entity;

@Entity
public class Orders extends BasicEntity {
    private Long productId;
    private Long optionId;
    private Long memberId;
    private String productName;
    private String productImageUrl;
    private String optionName;
    private int price;
    private int quantity;
    private String description;

    protected Orders() {}

    public Orders(Long productId, Long optionId, Long memberId, String productName, String productImageUrl, String optionName, int price, int quantity, String description) {
        this.productId = productId;
        this.optionId = optionId;
        this.memberId = memberId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.optionName = optionName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }
}
