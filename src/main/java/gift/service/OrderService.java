package gift.service;

import gift.model.entity.Item;
import gift.model.entity.Option;
import gift.model.entity.Order;
import gift.model.entity.User;
import gift.model.form.OrderForm;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final Long POINT_ACCUMULATION_RATE;

    public OrderService(UserService userService, OrderRepository orderRepository,
        ItemService itemService, @Value("${order.par}") Long POINT_ACCUMULATION_RATE) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.itemService = itemService;
        this.POINT_ACCUMULATION_RATE = POINT_ACCUMULATION_RATE;
    }

    @Transactional
    public Long executeOrder(Long userId, OrderForm form) {
        User user = userService.findUserEntityByUserId(userId);
        Item item = itemService.findItemById(form.getProductId());
        Option option = item.getOptionByOptionId(form.getOptionId());
        option.decreaseQuantity(form.getQuantity());
        user.decreasingPoint(form.getPoint());
        Long totalPrice = (item.getPrice() * form.getQuantity() - form.getPoint());
        Long accumulatePoint = calculatePoint(totalPrice);
        user.increasingPoint(accumulatePoint);
        Order order = new Order(user, form.getProductId(), form.getOptionId(), form.getQuantity(),
            form.getMessage(), LocalDateTime.now(), totalPrice);
        user.addOrder(order);
        return orderRepository.save(order).getId();
    }

    public Long calculatePoint(Long totalPrice) {
        return totalPrice / POINT_ACCUMULATION_RATE;
    }
}
