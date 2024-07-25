package gift.order.service;

import gift.order.model.OrderRepository;
import gift.order.model.dto.Order;
import gift.order.model.dto.OrderRequest;
import gift.order.model.dto.OrderResponse;
import gift.product.model.dto.option.Option;
import gift.product.service.OptionService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OptionService optionService;
    private final OrderRepository orderRepository;

    public OrderService(OptionService optionService, OrderRepository orderRepository) {
        this.optionService = optionService;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Option option = optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());
        Order order = new Order(option, orderRequest.quantity(), orderRequest.message());
        order = orderRepository.save(order);

        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(),
                order.getRegistrationDate(), order.getMessage());
    }
}
