package gift.DTO;

import gift.Entity.OrderEntity;

import java.time.LocalDateTime;

public class OrderDTO {

    private Long productId;
    private String productName;
    private int productPrice;
    private Long optionId;
    private Long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String optionName;

    private Long userId;

    public OrderDTO(Long productId, String productName, int productPrice, Long optionId, Long quantity, LocalDateTime createdAt, LocalDateTime updatedAt, String optionName) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.optionId = optionId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.optionName = optionName;
    }

    public OrderDTO(OrderEntity order) {
        this.userId = order.getUser().getId();
        this.optionId = order.getOption().getId();
        this.quantity = order.getQuantity();
    }

    // Getter 및 Setter 메서드

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}
