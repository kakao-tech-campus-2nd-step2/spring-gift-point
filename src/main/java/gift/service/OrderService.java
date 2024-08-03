package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.OptionResponse;
import gift.dto.response.OrderPaginationResponse;
import gift.dto.response.OrderResponse;
import gift.exception.customException.PointsNotAvailableException;
import gift.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static gift.exception.errorMessage.Messages.INSUFFICIENT_POINTS;
import static gift.exception.errorMessage.Messages.POINTS_USAGE_LIMIT;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, KakaoService kakaoService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @Transactional
    public OrderResponse orderOption(OrderRequest orderRequest, MemberRequest memberRequest){
        // 옵션 개수 차감
        optionService.subtractQuantityById(orderRequest.optionId(), orderRequest.quantity());

        // 위시리스트의 상품 삭제
        Long productId = optionService.findById(orderRequest.optionId()).productId();
        if(wishService.existsByProductId(productId)){
            wishService.deleteByProductId(productId);
        }

        // 옵션에서 상품 가격 찾아오기
        OptionResponse optionResponse = optionService.findById(orderRequest.optionId());
        int productPrice = optionService.toEntity(optionResponse)
                .getProduct()
                .getPrice();

        int price = productPrice * orderRequest.quantity();

        // 사용자 포인트 조회
        int pointsUsed = orderRequest.points();
        int memberPoints = memberService.getMemberPoints(memberRequest);
        if(pointsUsed > memberPoints) {
            throw new PointsNotAvailableException(INSUFFICIENT_POINTS);
        }

        if(pointsUsed > price/2) {
            throw new PointsNotAvailableException(POINTS_USAGE_LIMIT);
        }

        // 포인트 차감
        memberService.useMemberPoints(memberRequest,pointsUsed);

        // 포인트 더하기
        int pointsReceived = (int) Math.floor(price * 0.05);
        memberService.addMemberPoints(memberRequest.id(), pointsReceived);

        int payment = price - pointsUsed;

        // 주문 저장하기
        Order savedOrder = save(memberRequest, orderRequest, pointsUsed, pointsReceived, payment);

        // 주문 메시지 전송하기
        //String message = "옵션 id " + orderRequest.optionId() + " 상품이 주문되었습니다.";
        //kakaoService.sendKakaoMessage(accessToken,message);

        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public Order save(MemberRequest memberRequest, OrderRequest orderRequest, int pointsUsed, int pointsReceived, int payment){
        LocalDateTime orderDateTime = LocalDateTime.now();
        Member member = memberRequest.toEntity();
        Option option = optionService.toEntity(optionService.findById(orderRequest.optionId()));

        Order order =  new Order(orderRequest.quantity(),orderDateTime,orderRequest.message(),member,option,pointsUsed,pointsReceived,payment);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderPaginationResponse> getPagedOrders(MemberRequest memberRequest, int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sortParams[0]).with(Sort.Direction.fromString(sortParams[1]))));
        Page<Order> pagedOrders = orderRepository.findByMemberId(memberRequest.id(), pageable);
        return pagedOrders.map(OrderPaginationResponse::from);
    }
}
