package gift.classes.RequestState;

import gift.dto.OrderDto;

public class OrderRequestStateDTO extends RequestStateDTO {

    private final OrderDto orderDto;

    public OrderRequestStateDTO(RequestStatus requestStatus, String details, OrderDto orderDto) {
        super(requestStatus, details);
        this.orderDto = orderDto;
    }

    public OrderDto getOrderDto() {
        return orderDto;
    }
}
