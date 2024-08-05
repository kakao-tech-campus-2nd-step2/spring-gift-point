package gift.service;


import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    public Order getOrder(Long optionId){
        return orderRepository.findByOptionId(optionId);
    }
}
