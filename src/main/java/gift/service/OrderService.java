package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderResponse orderOption(OrderRequest orderRequest, MemberRequest memberRequest, String accessToken){
        optionService.subtractQuantityById(orderRequest.optionId(), orderRequest.quantity());

        Order savedOrder = save(memberRequest, orderRequest);

        Long productId = optionService.findById(orderRequest.optionId()).productId();
        if(wishService.existsByProductId(productId)){
            wishService.deleteByProductId(productId);
        }

        //String message = "옵션 id " + orderRequest.optionId() + " 상품이 주문되었습니다.";
        //kakaoService.sendKakaoMessage(accessToken,message);

        return new OrderResponse(savedOrder.getId(), savedOrder.getOption().getId(), savedOrder.getQuantity(), savedOrder.getOrderDateTime(), savedOrder.getMessage());
    }

    @Transactional
    public Order save(MemberRequest memberRequest, OrderRequest orderRequest){
        LocalDateTime orderDateTime = LocalDateTime.now();
        Member member = memberRequest.toEntity();
        Option option = optionService.toEntity(optionService.findById(orderRequest.optionId()));

        Order order =  orderRequest.toEntity(orderDateTime, member, option);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getPagedOrders(MemberRequest memberRequest, int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sortParams[0]).with(Sort.Direction.fromString(sortParams[1]))));
        Page<Order> pagedOrders = orderRepository.findByMemberId(memberRequest.id(), pageable);
        return pagedOrders.map(OrderResponse::from);
    }
}
