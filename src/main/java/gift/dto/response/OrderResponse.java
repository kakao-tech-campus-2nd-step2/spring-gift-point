package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.OrderDto;


public class OrderResponse {
    
    private OrderDto orderDto;

    @JsonCreator
    public OrderResponse(
        @JsonProperty("order")
        OrderDto orderDto
    ){
        this.orderDto = orderDto;
    }

    public OrderDto getOrderDto(){
        return orderDto;
    }

}
