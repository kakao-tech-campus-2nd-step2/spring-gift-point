package gift.domain.order.service;

import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.exception.InvalidOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemService orderItemService;
    private final MessageService messageService;

    public OrderService(
        OrderJpaRepository orderJpaRepository,
        OrderItemService orderItemService,
        MessageService messageService
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemService = orderItemService;
        this.messageService = messageService;
    }

    @Transactional
    public OrderResponse createAndSendMessage(OrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);

//        orderItemService.create(member, order, orderRequest.orderItems());
        Order savedOrder = orderJpaRepository.save(order);

        if (member.getAuthProvider() != AuthProvider.KAKAO) {
            throw new InvalidOrderException("error.invalid.userinfo.provider");
        }

//        if (!messageService.sendMessageToMe(member, OrderResponse.from(savedOrder)).equals("0")) {
//            throw new ExternalApiException("error.kakao.talk.message.response");
//        };
        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);

        OrderItemRequest orderItemRequest = new OrderItemRequest(null, orderRequest.optionId(),
            orderRequest.quantity());
        orderItemService.createOne(member, order, orderItemRequest);

        Order savedOrder = orderJpaRepository.save(order);
        return OrderResponse.from(savedOrder);
    }
}
