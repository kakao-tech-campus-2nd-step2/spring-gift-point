package gift.order.service;

import gift.common.auth.JwtUtil;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.option.domain.Option;
import gift.order.domain.Order;
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

    private final OptionJpaRepository optionJpaRepository;
    private final WishService wishService;
    private final OrderJPARepository orderJPARepository;
    private final ProductService productService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public OrderService(OptionJpaRepository optionJpaRepository, WishService wishService, OrderJPARepository orderJPARepository, ProductService productService, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.optionJpaRepository = optionJpaRepository;
        this.wishService = wishService;
        this.orderJPARepository = orderJPARepository;
        this.productService = productService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    // 주문하기 로직
    @Transactional
    public OrderResponse requestOrder(String token, @Valid OrderRequest orderRequest) {
        // 0. optionId로 상품 정보 조회
        Product orderProduct = productService.getProductByOptionId(orderRequest.getOptionId());

        // 1. 토큰으로 사용자 정보 조회
        String email = jwtUtil.extractEmail(token);
        Member member = memberRepository.findByEmail(email).orElse(null);

        // 2. 검증 로직
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        String request_message = orderRequest.getMessage();
        Long points_used = orderRequest.getPoints();
        /// 2-0. 사용자 정보가 있는지 확인
        if (member == null) {
            throw new IllegalArgumentException("[ERROR] 사용자 정보가 없음");
        }
        /// 2-1. 사용하려는 포인트가 가지고 있는 포인트보다 크진 않은지 확인
        if (points_used > member.getPoints()) {
            throw new IllegalArgumentException("[ERROR] 사용하려는 포인트가 보유 포인트보다 큼");
        }
        /// 2-2. 주문 상품의 재고가 충분한지 확인
        Option option = optionJpaRepository.findById(request_optionId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + request_optionId));
        if (request_quantity > option.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] 주문 수량이 재고 수량을 초과함");
        }

        // 3. 주문 로직
        /// 3-1. 주문 수량만큼 옵션 재고 감소, 사용자 포인트 감소
        option.subtract(request_quantity); // 주문 수량만큼 옵션 재고 감소
        member.subtractPoints(orderRequest.getPoints()); // 사용자 포인트 감소
        /// 3-2. 주문 상품이 위시리스트에 있으면 삭제
        Long productId = orderProduct.getId(); // 상품 id
        if (wishService.getWishByProductId(productId) != null) {
            wishService.deleteWish(productId); // 위시리스트에 해당 상품이 있으면 삭제
        }
        // 3-3. 주문 내역 저장
        Long productPrice = orderProduct.getPrice() - orderRequest.getPoints(); // 상품 가격
        Long payment = productPrice * request_quantity; // 결제 금액
        if (payment * 0.5 < points_used) {
            throw new IllegalArgumentException("[ERROR] 사용하려는 포인트가 결제 금액의 50%를 초과함");
        }
        Long pointsReceived = (long) (productPrice * 0.05); // 적립 포인트
        orderJPARepository.save(new Order(request_optionId, request_quantity, LocalDateTime.now(), request_message, points_used, pointsReceived, payment));
        Long orderId = orderJPARepository.findByOptionId(request_optionId).getId();
        // 3-4. 5% 적립
        member.addPoints(pointsReceived); // 5% 적립

        // 4. 사용자 포인트를 저장
        memberRepository.save(member);

        return new OrderResponse(orderId, request_optionId, request_quantity, LocalDateTime.now(), request_message, points_used, pointsReceived, payment);
    }

    // 주문 목록 조회 (페이지네이션 적용)
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderByPage(int page, int size, String sortBy, String direction) {
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
        return orderPage.map(order -> new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                order.getPointsUsed(),
                order.getPointsReceived(),
                order.getPayment()
        ));
    }
}
