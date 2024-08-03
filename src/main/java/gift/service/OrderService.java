package gift.service;

import gift.domain.model.dto.OrderAddRequestDto;
import gift.domain.model.dto.OrderResponseDto;
import gift.domain.model.entity.Option;
import gift.domain.model.entity.Order;
import gift.domain.model.entity.Point;
import gift.domain.model.entity.User;
import gift.domain.repository.OptionRepository;
import gift.domain.repository.OrderRepository;
import gift.domain.repository.PointRepository;
import gift.domain.repository.UserRepository;
import gift.domain.repository.WishRepository;
import gift.exception.InsufficientQuantityException;
import gift.exception.NoSuchOptionException;
import gift.util.SortUtil;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoMessageService kakaoMessageService;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, KakaoMessageService kakaoMessageService,
        UserRepository userRepository, PointRepository pointRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
    }

    @Transactional
    public OrderResponseDto addOrder(User user, OrderAddRequestDto orderAddRequestDto) {
        Option option = optionRepository.findById(orderAddRequestDto.getOptionId())
            .orElseThrow(() -> new NoSuchOptionException(
                "해당 옵션이 존재하지 않습니다: " + orderAddRequestDto.getOptionId()));

        if (option.getQuantity() < orderAddRequestDto.getQuantity()) {
            throw new InsufficientQuantityException(
                "해당 옵션의 재고가 부족합니다: " + orderAddRequestDto.getOptionId());
        }

        int orderAmount = option.getProduct().getPrice() * orderAddRequestDto.getQuantity();
        if (orderAddRequestDto.getPoint() > orderAmount) {
            throw new IllegalArgumentException("사용하려는 포인트가 주문 금액을 초과합니다.");
        }

        // 수량 차감
        option.subtract(orderAddRequestDto.getQuantity());
        optionRepository.save(option);

        // 주문 생성
        Order order = new Order(user, option, orderAddRequestDto.getQuantity(), LocalDateTime.now(),
            orderAddRequestDto.getMessage());
        Order savedOrder = orderRepository.save(order);

        // 포인트 차감
        int usedPoints = orderAddRequestDto.getPoint();
        user.usePoints(usedPoints);
        Point usedPointRecord = new Point(user, -usedPoints, Point.PointType.USE,
            user.getPointBalance());
        pointRepository.save(usedPointRecord);

        // 포인트 적립
        int earnedPoints = (int) (orderAmount * 0.01);
        user.addPoints(earnedPoints);
        Point earnedPointRecord = new Point(user, earnedPoints, Point.PointType.EARN,
            user.getPointBalance());
        pointRepository.save(earnedPointRecord);

        // 변경된 User 엔티티 저장
        userRepository.save(user);

        // 위시리스트에서 제거
        wishRepository.deleteByUserAndProduct(user, option.getProduct());

        // 나에게 카카오톡 메시지 전송
        kakaoMessageService.sendOrderMessage(user.getKakaoAccessToken(),
            option.getProduct().getName(), option.getName(), orderAddRequestDto.getQuantity(),
            orderAddRequestDto.getMessage(), option.getProduct().getImageUrl());

        return new OrderResponseDto(
            savedOrder.getId(),
            savedOrder.getOption().getId(),
            savedOrder.getQuantity(),
            savedOrder.getOrderDateTime(),
            savedOrder.getMessage()
        );
    }

    public Page<OrderResponseDto> getOrders(int page, int size, String sort, User user) {
        Sort sortObj = SortUtil.createSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Order> orders = orderRepository.findByUser(user, pageable);

        return orders.map(order -> new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        ));
    }
}
