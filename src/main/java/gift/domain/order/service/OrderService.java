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
import gift.domain.order.entity.Price;
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
    public MultipleOrderResponse createMultipleAndSendMessage(MultipleOrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);

        Price originalPrice = orderItemService.createMultiple(member, order, orderRequest.orderItems());
        Order savedOrder = purchase(member, originalPrice, order);

        if (member.getAuthProvider() != AuthProvider.KAKAO) {
            throw new InvalidOrderException("error.invalid.userinfo.provider");
        }

        MultipleOrderResponse response = MultipleOrderResponse.from(savedOrder, originalPrice.value());
        messageService.sendMessageToMe(member, response).equals("0");
        return response;
    }

    @Transactional
    public OrderResponse createOne(OrderRequest orderRequest, Member member) {
        Order order = orderRequest.toOrder(member);
        OrderItemRequest orderItemRequest = new OrderItemRequest(orderRequest.optionId(), orderRequest.quantity());

        Price originalPrice = orderItemService.createOne(member, order, orderItemRequest);
        Order savedOrder = purchase(member, originalPrice, order);
        return OrderResponse.from(savedOrder, originalPrice.value());
    }

    private Order purchase(Member member, Price originalPrice, Order order) {
        Price purchasePrice = applyDiscountPolicy(originalPrice, DiscountPolicy.DEFAULT);
        order.assignPurchasePrice(purchasePrice);
        member.usePoint(purchasePrice);

        return orderJpaRepository.save(order);
    }

    private Price applyDiscountPolicy(Price originalPrice, DiscountPolicy discountPolicy) {
        Price purchasePrice = originalPrice;

        if (discountPolicy == DiscountPolicy.DEFAULT) {
            if (originalPrice.value() >= 50000) {
                purchasePrice = originalPrice.multiply(0.9);
            }
        }
        return purchasePrice;
    }
}
