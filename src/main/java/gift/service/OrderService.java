package gift.service;

import gift.domain.*;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final KakaoService kakaoService;
    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final PointService pointService;

    public OrderService(OrderRepository orderRepository, WishRepository wishRepository, KakaoService kakaoService, OptionService optionService, OptionRepository optionRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.kakaoService = kakaoService;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.pointService = pointService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest, Member member) throws Exception {
        Option option = optionRepository.findById(orderRequest.getOptionId())
                .orElseThrow(() -> new Exception("해당하는 옵션이 없습니다."));

        if (option.getQuantity() < orderRequest.getQuantity()) {
            throw new IllegalArgumentException("옵션 잔여 개수가 요청 개수보다 적습니다.");
        }

        optionService.subtractOptionQuantity(option.getProduct().getId(), option.getName(), orderRequest.getQuantity());

        optionRepository.save(option);

        int productPrice = (int) (option.getProduct().getPrice() * orderRequest.getQuantity());
        int memberPoints = member.getPoint();

        if (memberPoints >= productPrice) {
            pointService.subtractPoint(member.getId(), productPrice);
        } else {
            pointService.subtractPoint(member.getId(), memberPoints);
        }

        Order order = new Order(member, option, orderRequest.getQuantity(), LocalDateTime.now(), orderRequest.getMessage());
        orderRepository.save(order);

        Wish wish = wishRepository.findByProductIdAndMemberId(option.getProduct().getId(), member.getId());

        if (wish != null) {
            wishRepository.delete(wish);
        }

        if (member.getEmail() != null) {
            kakaoService.sendOrderMessage(member.getId(), order);
        }

        return new OrderResponse(order.getId(), option.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());

    }

    public Page<OrderResponse> getOrdersByMemberId(Long memberId, PageRequest pageRequest) {
        Page<Order> orders = orderRepository.findByMemberId(memberId, pageRequest);
        return orders.map(order -> new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage()));
    }

}
