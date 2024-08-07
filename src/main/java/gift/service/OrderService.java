package gift.service;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.dto.point.PointRequest;
import gift.entity.Order;
import gift.entity.Point;
import gift.exception.CustomException;
import gift.repository.OrderRepository;
import gift.repository.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final PointService pointService;
    private final PointRepository pointRepository;
    private final MemberService memberService;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, PointService pointService, PointRepository pointRepository, MemberService memberService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.pointService = pointService;
        this.pointRepository = pointRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        optionService.decreaseOptionQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        if (orderRequest.getPoint() > 0) {
            Point point = pointRepository.findByMemberEmail(orderRequest.getEmail())
                    .orElseThrow(() -> new CustomException.EntityNotFoundException("Points not found for member"));

            point.deductPoints(orderRequest.getPoint());
            pointRepository.save(point);
        }

        Order order = new Order.Builder()
                .optionId(orderRequest.getOptionId())
                .quantity(orderRequest.getQuantity())
                .orderTime(orderRequest.getOrderTime())
                .message(orderRequest.getMessage())
                .email(orderRequest.getEmail())
                .build();
        order = orderRepository.save(order);

        int pointsToAdd = (int) (orderRequest.getQuantity() * productService.getProductPrice(orderRequest.getProductId()) * 0.03);
        PointRequest pointRequest = new PointRequest();
        pointRequest.setMemberId(memberService.getMemberIdByEmail(orderRequest.getEmail()));
        pointRequest.setPoints(pointsToAdd);
        pointService.addPoint(pointRequest);

        wishService.deleteByProductId(orderRequest.getProductId());

        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                orderRequest.getPoint(),
                order.getOrderTime(),
                order.getMessage()
        );
    }

    public Page<OrderResponse> getOrdersByEmail(String email, Pageable pageable) {
        Page<Order> orders = orderRepository.findByEmail(email, pageable);

        return orders.map(order -> new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                0,
                order.getOrderTime(),
                order.getMessage()
        ));
    }
}
