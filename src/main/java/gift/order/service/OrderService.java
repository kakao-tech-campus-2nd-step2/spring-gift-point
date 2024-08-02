package gift.order.service;

import gift.common.auth.JwtUtil;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.member.service.MemberService;
import gift.option.domain.Option;
import gift.option.repository.OptionRepository;
import gift.order.domain.Order;
import gift.order.dto.OrderPageResponse;
import gift.order.dto.OrderResponse;
import gift.option.repository.OptionJpaRepository;
import gift.order.dto.OrderRequest;
import gift.order.repository.OrderJPARepository;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishService wishService;
    private final OrderJPARepository orderJPARepository;
    private final ProductService productService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public OrderService(OptionRepository optionRepository, WishService wishService, OrderJPARepository orderJPARepository, ProductService productService, MemberRepository memberRepository, MemberService memberService) {
        this.optionRepository = optionRepository;
        this.wishService = wishService;
        this.orderJPARepository = orderJPARepository;
        this.productService = productService;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    private static final double POINT_RECEIVE_RATE = 0.05;
    private static final double POINT_PRODUCT_RATE = 0.5;

    // 주문하기 로직
    @Transactional
    public OrderResponse requestOrder(String token, @Valid OrderRequest orderRequest) {
        // 0. optionId로 상품 정보 조회
        Product orderProduct = productService.getProductByOptionId(orderRequest.getOptionId());

        // 1. 토큰으로 사용자 정보 조회
        Member member = memberService.getMemberByToken(token);

        // 2. 주문 검증
        isValidOrderRequest(orderRequest, member);

        // 3. 주문 로직
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        String request_message = orderRequest.getMessage();
        Long points_used = orderRequest.getPoints();
        Option option = optionRepository.findById(request_optionId);
        /// 3-1. 주문 수량만큼 옵션 재고 감소, 사용자 포인트 감소
        option.subtract(request_quantity); // 주문 수량만큼 옵션 재고 감소
        member.subtractPoints(orderRequest.getPoints()); // 사용자 포인트 감소
        optionRepository.save(option);
        /// 3-2. 주문 상품이 위시리스트에 있으면 삭제
        Long productId = orderProduct.getId(); // 상품 id
        if (wishService.getWishByProductId(productId) != null) {
            wishService.deleteWish(productId); // 위시리스트에 해당 상품이 있으면 삭제
        }
        // 3-3. 주문 내역 저장
        Long productPrice = orderProduct.getPrice() - orderRequest.getPoints(); // 상품 가격
        Long payment = productPrice * request_quantity; // 결제 금액
        if (payment * POINT_PRODUCT_RATE < points_used) {
            throw new IllegalArgumentException("[ERROR] 사용하려는 포인트가 결제 금액의 50%를 초과함");
        }
        Long pointsReceived = (long) (productPrice * POINT_RECEIVE_RATE); // 적립 포인트
        orderJPARepository.save(new Order(request_optionId, request_quantity, LocalDateTime.now(), request_message, points_used, pointsReceived, payment));
        Long orderId = orderJPARepository.findByOptionId(request_optionId).getId();
        // 3-4. 5% 적립하고 사용자 포인트 저장
        addPoints(member, pointsReceived);

        return new OrderResponse(orderId, request_optionId, request_quantity, LocalDateTime.now(), request_message, points_used, pointsReceived, payment);
    }

    // 포인트 적립하기
    public void addPoints(Member member, Long points) {
        member.addPoints(points);
        memberRepository.save(member);
    }

    // 올바른 주문인지 검증하기
    public void isValidOrderRequest(OrderRequest orderRequest, Member member) {
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        /// 사용하려는 포인트가 가지고 있는 포인트보다 크진 않은지 확인
        if (orderRequest.getPoints() > member.getPoints()) {
            throw new IllegalArgumentException("[ERROR] 사용하려는 포인트가 보유 포인트보다 큼");
        }
        /// 주문 상품의 재고가 충분한지 확인
        Option option = optionRepository.findById(request_optionId);
        if (request_quantity > option.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] 주문 수량이 재고 수량을 초과함");
        }
    }

    // 주문 목록 조회 (페이지네이션 적용)
    @Transactional(readOnly = true)
    public Page<OrderPageResponse> getOrderByPage(int page, int size, String sortBy, String direction) {
        // validation
        if (page < 0 || size < 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        // sorting
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        // paging
        Pageable pageable = PageRequest.of(page, size, sort); // Pageable 객체 생성
        Page<Order> orderPage = orderJPARepository.findAll(pageable); // Page<Order> 타입의 객체를 생성

        // Order 엔티티를 OrderResponse로 변환하고 Page 객체로 반환
        return orderPage.map(order -> new OrderPageResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        ));
    }
}
