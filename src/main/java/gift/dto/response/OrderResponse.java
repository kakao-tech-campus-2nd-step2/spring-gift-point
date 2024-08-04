package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.OrderDto;


public class OrderResponse {
    
    private OrderDto order;

    @JsonCreator
    public OrderResponse(
        @JsonProperty("order")
        OrderDto order
    ){
        this.order = order;
    }

    public OrderDto getOrder(){
        return order;
    }

}
