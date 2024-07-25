package gift.order.service;

import gift.product.service.OptionService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OptionService optionService;

    public OrderService(OptionService optionService) {
        this.optionService = optionService;
    }

    public void createOrder() {
        
    }
}
