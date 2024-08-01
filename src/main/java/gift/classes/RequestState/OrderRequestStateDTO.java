package gift.classes.RequestState;

import gift.dto.OrderDto;
import org.springframework.http.HttpStatus;

public class OrderRequestStateDTO extends RequestStateDTO {

    private final OrderDto data;

    public OrderRequestStateDTO(HttpStatus status, String message, OrderDto data) {
        super(status, message);
        this.data = data;
    }

    public OrderDto getData() {
        return data;
    }
}
