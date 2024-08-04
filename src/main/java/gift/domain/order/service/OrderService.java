package gift.domain.order.service;

import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.MultipleOrderRequest;
import gift.domain.order.dto.MultipleOrderResponse;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.DiscountPolicy;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.exception.ExternalApiException;
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
    public MultipleOrderResponse createMultipleAndSendMessage(MultipleOrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);

        int originalPrice = orderItemService.createMultiple(member, order, orderRequest.orderItems());
        Order savedOrder = purchase(member, originalPrice, order);

        if (member.getAuthProvider() != AuthProvider.KAKAO) {
            throw new InvalidOrderException("error.invalid.userinfo.provider");
        }

        MultipleOrderResponse response = MultipleOrderResponse.from(savedOrder, originalPrice);
        if (!messageService.sendMessageToMe(member, response).equals("0")) {
            throw new ExternalApiException("error.kakao.talk.message.response");
        };
        return response;
    }

    @Transactional
    public OrderResponse createOne(OrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);
        OrderItemRequest orderItemRequest = new OrderItemRequest(orderRequest.optionId(), orderRequest.quantity());

        int originalPrice = orderItemService.createOne(member, order, orderItemRequest);
        Order savedOrder = purchase(member, originalPrice, order);
        return OrderResponse.from(savedOrder, originalPrice);
    }

    private Order purchase(Member member, int originalPrice, Order order) {
        int purchasePrice = applyDiscountPolicy(originalPrice, DiscountPolicy.DEFAULT);
        order.setPurchasePrice(purchasePrice);
        member.usePoint(purchasePrice);

        return orderJpaRepository.save(order);
    }

    private int applyDiscountPolicy(int originalPrice, DiscountPolicy discountPolicy) {
        int purchasePrice = originalPrice;
        if (discountPolicy == DiscountPolicy.DEFAULT) {
            if (originalPrice >= 50000) {
                purchasePrice = (int) (originalPrice * 0.9);
            }
        }
        return purchasePrice;
    }
}
