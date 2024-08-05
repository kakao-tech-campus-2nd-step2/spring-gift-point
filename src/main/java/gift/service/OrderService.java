package gift.service;


import gift.entity.OrderItem;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    public OrderItem getOrder(Long optionId){
        return orderRepository.findByOptionId(optionId);
    }
}
