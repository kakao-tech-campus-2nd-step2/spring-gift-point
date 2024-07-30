package gift.api.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.api.order.domain.Order;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.TemplateObject;
import gift.api.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public LinkedMultiValueMap<Object, Object> createBody(OrderRequest orderRequest) {
        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        TemplateObject templateObject = TemplateObject.of(orderRequest.message(), "", "", "바로 확인");
        try {
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException ignored) {
        }
        return body;
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
