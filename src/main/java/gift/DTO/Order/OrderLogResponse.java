package gift.DTO.Order;

import gift.DTO.Product.ProductResponse;

import java.util.Locale;

public class OrderLogResponse {
    private String name;
    private int price;
    private int quantity;
    private String imageUrl;
    private String orderDateTime;

    public OrderLogResponse(ProductResponse productResponse, OrderResponse orderResponse) {
        this.name = productResponse.getName();
        this.price = productResponse.getPrice();
        this.quantity = orderResponse.getQuantity();
        this.imageUrl = productResponse.getImageUrl();
        this.orderDateTime = orderResponse.getOrderDateTime();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }
}
