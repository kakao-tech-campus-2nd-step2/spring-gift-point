package gift.dto.order;

import lombok.Setter;

import java.time.LocalDateTime;

public class OrderRequest {
    private Long productId;
    private Long optionId;
    private int quantity;
    private String message;
    private LocalDateTime orderTime;
    private String email;
    private int point;
    @Setter
    private boolean usePoints;

    public OrderRequest() {}

    public OrderRequest(Long productId, Long optionId, int quantity, int point, String message, LocalDateTime orderTime, String email) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.point = point;
        this.message = message;
        this.orderTime = orderTime;
        this.email = email;
    }

    public OrderRequest(Long productId, Long optionId, int quantity, boolean usePoints, String message, LocalDateTime orderTime, String email) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.usePoints = usePoints;
        this.message = message;
        this.orderTime = orderTime;
        this.email = email;
        this.point = 0;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getEmail() {
        return email;
    }

    public int getPoint() {
        return point;
    }

    public boolean isUsePoints() {
        return usePoints;
    }

}
