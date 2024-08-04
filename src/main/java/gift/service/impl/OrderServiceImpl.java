package gift.service.impl;

import gift.model.Order;
import gift.model.Member;
import gift.model.Option;
import gift.repository.OrderRepository;
import gift.repository.OptionRepository;
import gift.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public Order createOrder(Order order, Member member) {
        order.setOrderDateTime(LocalDateTime.now());

        Option option = order.getOption();

        int unitPrice = option.getProduct().getPrice();
        int originalTotalPrice = unitPrice * order.getQuantity();

        if (originalTotalPrice >= 50000) {
            originalTotalPrice *= 0.9; // 10% 할인
        }

        int pointsToUse = order.getPointsUsed();
        int paymentAmount = originalTotalPrice;
        if (pointsToUse > 0) {
            if (member.getPoints() < pointsToUse) {
                throw new IllegalArgumentException("Not enough points");
            }
            member.subtractPoints(pointsToUse);
            paymentAmount -= pointsToUse;
        }

        order.setPaymentAmount(paymentAmount);

        int pointsToAdd = (int) Math.round(paymentAmount * 0.01);
        member.addPoints(pointsToAdd);

        if (order.isCashReceipt()) {
            issueCashReceipt(order.getPhoneNumber(), BigDecimal.valueOf(paymentAmount));
        }

        return orderRepository.save(order);
    }

    private void issueCashReceipt(String phoneNumber, BigDecimal amount) {
        System.out.println("현금영수증 발행 완료: 전화번호=" + phoneNumber + ", 금액=" + amount);
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
