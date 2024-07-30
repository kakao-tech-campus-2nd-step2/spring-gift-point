package gift.service;

import gift.entity.Product;
import gift.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Product> getProductPage(int page, int size, String[] sort) {
        List<Order> sorts = new ArrayList<>();

        if (sort[1].equals("asc")) {
            sorts.add(Sort.Order.asc(sort[0]));
        }
        if (sort[1].equals("desc")) {
            sorts.add(Sort.Order.desc(sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return orderRepository.findAll(pageable);
    }

}
