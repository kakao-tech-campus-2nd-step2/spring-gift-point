package gift.order.controller;

import gift.kakao.login.dto.KakaoUser;
import gift.order.domain.OrderListDTO;
import gift.product.domain.Product;
import gift.product.option.service.OptionService;
import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.service.OrderService;
import gift.product.service.ProductService;
import gift.user.repository.UserRepository;
import gift.utility.JwtUtil;
import gift.wish.domain.WishlistResponse;
import gift.wish.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 API")
public class OrderController {
    private final OrderService orderService;
    private final OptionService optionService;
    private final UserRepository userRepository;
    private final WishlistService wishlistService;
    private final ProductService productService;

    public OrderController(OrderService orderService, OptionService optionService,
        UserRepository userRepository, WishlistService wishlistService,
        ProductService productService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.userRepository = userRepository;
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "주문하기")
    public ResponseEntity<OrderResponse> getOrder(
        @RequestBody OrderRequest orderRequest,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String email = JwtUtil.extractEmail(jwtToken);
        KakaoUser kakaoUser = (KakaoUser) userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("jwtToken or Email " + email + "이 없습니다."));
        Long userId = kakaoUser.getId();
        // 1. 주문 저장
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        // 2. 옵션 수량 차감, wishlist에서 제거
        optionService.subtract(optionService.convertToDTO(optionService.findById(orderResponse.optionId())),
                                orderResponse.quantity());
        wishlistService.deleteByUserIdProductId(userId, optionService.findById(orderResponse.optionId()).getProduct().getId());
        // 3. 카카오톡 메시지 api 전송
        orderService.sendMessage(kakaoUser.getToken(), orderRequest.message());
        // 4. 포인트 차감
        // 4-1. 주문 가격 계산
        Product product = productService.getProductById(orderRequest.productId())
                .orElseThrow(() -> new IllegalArgumentException("productId " + orderRequest.productId() + "가 없습니다."));
        Long price = product.getPrice();
        Long totalPayment = price * orderRequest.quantity();
        long actualPayment = totalPayment - orderRequest.point();
        kakaoUser.setPoint(kakaoUser.getPoint() - orderRequest.point() + (int)(actualPayment * 0.01));
        // 5. response 반환
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문목록 조회")
    public ResponseEntity<Page<OrderListDTO>> getWishlist(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "orderDateTime,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<OrderListDTO> orderPages = orderService.getOrderListDTOs(pageable);
        return new ResponseEntity<>(orderPages, HttpStatus.OK);
    }
}
