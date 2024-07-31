package gift.order.service;

import gift.kakao.login.dto.KakaoMessageSendResponse;
import gift.kakao.login.service.KakaoLoginService;
import gift.order.domain.OrderListDTO;
import gift.product.domain.ProductResponse;
import gift.product.option.domain.Option;
import gift.product.option.repository.OptionRepository;
import gift.order.domain.Order;
import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.repository.OrderRepository;
import gift.product.service.ProductService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final ProductService productService;
    private final KakaoLoginService kakaoLoginService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        ProductService productService, KakaoLoginService kakaoLoginService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.productService = productService;
        this.kakaoLoginService = kakaoLoginService;
    }

    public OrderResponse createOrder(OrderRequest orderRequest){
        Order order = save(convertRequestToEntity(orderRequest));
        return convertEntityToResponse(order);
    }
    public Order save(Order order){
        return orderRepository.save(order);
    }

    public KakaoMessageSendResponse sendMessage(String accessToken, String message){
        return kakaoLoginService.sendMessage(accessToken, message);
    }
    public Page<OrderResponse> getOrderResponses(Pageable pageable) {
        return orderRepository.findAll(pageable)
            .map(this::convertEntityToResponse);
    }
    public Page<OrderListDTO> getOrderListDTOs(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        List<OrderListDTO> orderListDTOs = ordersPage.stream()
            .map(order -> {
                ProductResponse productResponse = productService.convertToResponse(order.getOption().getProduct());
                return new OrderListDTO(
                    productResponse.name(),
                    productResponse.price(),
                    order.getQuantity(),
                    productResponse.imageUrl(),
                    order.getOrderDateTime());
            })
            .collect(Collectors.toList());

        return new PageImpl<>(orderListDTOs, pageable, ordersPage.getTotalElements());
    }
    private Order convertRequestToEntity(OrderRequest orderRequest){
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new IllegalArgumentException("OrderService: OptionId가 없다."));
        String orderDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        return new Order(orderRequest.quantity(), orderDateTime, orderRequest.message(), option);
    }
    private OrderResponse convertEntityToResponse(Order order){
        return new OrderResponse(order.getId(),
                order.getOption().getId(),
                        order.getQuantity(),
                        order.getOrderDateTime(),
                        order.getMessage());
    }

}
