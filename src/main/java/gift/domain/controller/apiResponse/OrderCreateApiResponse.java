package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OrderResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class OrderCreateApiResponse extends BasicApiResponse {

    private final OrderResponse order;

    public OrderCreateApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "order", required = true) OrderResponse order
    ) {
        super(statusCode);
        this.order = order;
    }

    public OrderResponse getOrder() {
        return order;
    }
}
