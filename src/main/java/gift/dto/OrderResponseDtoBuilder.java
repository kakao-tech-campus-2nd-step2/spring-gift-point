package gift.dto;

import java.time.LocalDateTime;

public class OrderResponseDtoBuilder {
    private int id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDtoBuilder id(int id){
        this.id = id;
        return this;
    }

    public OrderResponseDtoBuilder optionId(Long optionId){
        this.optionId = optionId;
        return this;
    }

    public OrderResponseDtoBuilder quantity(int quantity){
        this.quantity = quantity;
        return this;
    }

    public OrderResponseDtoBuilder orderDateTime(LocalDateTime orderDateTime){
        this.orderDateTime = orderDateTime;
        return this;
    }

    public OrderResponseDtoBuilder message(String message){
        this.message = message;
        return this;
    }

    public OrderResponseDto build() {
        return new OrderResponseDto(id, optionId, quantity, orderDateTime, message);
    }
}
