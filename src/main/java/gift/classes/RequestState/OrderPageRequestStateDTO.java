package gift.classes.RequestState;

import gift.dto.OrderPageDto;
import org.springframework.http.HttpStatus;

public class OrderPageRequestStateDTO extends RequestStateDTO {

    private final OrderPageDto data;

    public OrderPageRequestStateDTO(HttpStatus status, String message, OrderPageDto data) {
        super(status, message);
        this.data = data;
    }

    public OrderPageDto getData() {
        return data;
    }
}
