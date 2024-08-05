package gift.order.domain;

public class OrderResponse {
    private Long id;
    private Long productId;
    private String name;
    private String imageUrl;
    private Long optionId;
    private Long count;
    private Long price;
    private String orderDateTime;
    private String message;
    private boolean success;

    public OrderResponse(Long id, Long productId, String name, Long optionId, Long count, String imageUrl,Long price, String orderDateTime, String message, boolean success) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.optionId = optionId;
        this.count = count;
        this.price = price;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getCount() {
        return count;
    }

    public Long getPrice() {
        return price;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
