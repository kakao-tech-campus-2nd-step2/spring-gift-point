package gift.converter;

import gift.api.OrderRequest;
import gift.dto.OrderDTO;

public class OrderConverter {

    public static OrderDTO convertToDTO(OrderRequest orderRequest) {
        return new OrderDTO(
            orderRequest.getOrderId(),
            orderRequest.getOptionId(),
            orderRequest.getQuantity(),
            orderRequest.getMessage()
        );
    }

    public static OrderRequest convertToEntity(OrderDTO orderDTO) {
        return new OrderRequest(
            orderDTO.getOrderId(),
            orderDTO.getOptionId(),
            orderDTO.getQuantity(),
            orderDTO.getMessage()
        );
    }
}