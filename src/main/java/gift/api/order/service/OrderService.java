package gift.api.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.api.member.service.KakaoService;
import gift.api.option.dao.OptionDao;
import gift.api.option.service.OptionService;
import gift.api.order.domain.Order;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.dto.TemplateObject;
import gift.api.order.repository.OrderRepository;
import gift.api.wishlist.dto.WishDeleteRequest;
import gift.api.wishlist.service.WishService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class OrderService {

    private final KakaoService kakaoService;
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final OptionDao optionDao;
    private final WishService wishService;

    public OrderService(KakaoService kakaoService, OrderRepository orderRepository,
        OptionService optionService, OptionDao optionDao, WishService wishService) {
        this.kakaoService = kakaoService;
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.optionDao = optionDao;
        this.wishService = wishService;
    }

    public OrderResponse order(Long memberId, OrderRequest orderRequest) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            WishDeleteRequest.of(optionDao.findOptionById(orderRequest.optionId()).getProduct().getId()));
        Order order = orderRepository.save(
            orderRequest.toEntity(optionDao.findOptionById(orderRequest.optionId()),
                orderRequest.message()));
        kakaoService.sendMessage(memberId, createBody(orderRequest));
        return OrderResponse.of(order);
    }

    private LinkedMultiValueMap<Object, Object> createBody(OrderRequest orderRequest) {
        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        TemplateObject templateObject = TemplateObject.of(orderRequest.message(), "", "", "바로 확인");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException ignored) {
        }
        return body;
    }
}
