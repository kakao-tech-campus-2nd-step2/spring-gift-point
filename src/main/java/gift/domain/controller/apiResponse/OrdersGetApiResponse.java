package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OrderResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class OrdersGetApiResponse extends BasicApiResponse {

    private final List<OrderResponse> orders;

    public OrdersGetApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "orders", required = true) List<OrderResponse> orders
    ) {
        super(statusCode);
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
