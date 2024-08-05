package gift.api.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.api.member.domain.Member;
import gift.api.option.domain.Option;
import gift.api.order.domain.Order;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.dto.TemplateObject;
import gift.api.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<OrderResponse> getOptions(Long memberId) {
        return orderRepository.findAllByMemberId(memberId)
            .stream()
            .map(order -> OrderResponse.of(order, null))
            .collect(Collectors.toList());
    }

    @Transactional
    public Order save(Member member, Option option, OrderRequest orderRequest) {
        return orderRepository.save(orderRequest.toEntity(member, option));
    }

    public Integer getPointsToSave(Integer price, Integer quantity) {
        return (int) Math.floor(price * quantity * 0.01);
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
}
