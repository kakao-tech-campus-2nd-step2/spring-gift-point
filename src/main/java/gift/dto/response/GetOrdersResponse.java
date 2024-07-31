package gift.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.OrderDto;

public class GetOrdersResponse {
    
    private List<OrderDto> orders;

    @JsonCreator
    public GetOrdersResponse(
        @JsonProperty("orders")
        List<OrderDto> orders
    ){
        this.orders = orders;
    }

    public List<OrderDto> getOrders(){
        return orders;
    }
}
