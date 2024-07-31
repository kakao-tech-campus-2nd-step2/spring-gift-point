package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OrderResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class OrderCreateApiResponse extends BasicApiResponse {

    private final OrderResponse createdOrder;

    public OrderCreateApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "created-order", required = true) OrderResponse createdOrder
    ) {
        super(statusCode);
        this.createdOrder = createdOrder;
    }

    public OrderResponse getCreatedOrder() {
        return createdOrder;
    }
}
