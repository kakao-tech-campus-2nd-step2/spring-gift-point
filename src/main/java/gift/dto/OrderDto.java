package gift.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.entity.Order;


public class OrderDto {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    @JsonCreator
    public OrderDto(
        @JsonProperty("id") Long id,
        @JsonProperty("option_id") Long optionId,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("order_date_time") LocalDateTime orderDateTime,
        @JsonProperty("message") String message
    ) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public static OrderDto fromEntity(Order order){
        return new OrderDto(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderTime(), order.getMessage());
    }
}
