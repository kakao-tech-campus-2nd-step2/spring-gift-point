package gift.order.controller;

import gift.product.option.service.OptionService;
import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.service.OrderService;
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

    private final WishlistService wishlistService;

    public OrderController(OrderService orderService, OptionService optionService,
        WishlistService wishlistService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
    }

    @PostMapping
    @Operation(summary = "주문하기")
    public ResponseEntity<OrderResponse> getOrder(
        @RequestBody OrderRequest orderRequest,
        @RequestParam("userId") Long userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String jwtAccessToken = authorizationHeader.replace("Bearer ", "");
        // 1. 주문 저장
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        // 2. 옵션 수량 차감, wishlist에서 제거
        optionService.subtract(optionService.convertToDTO(optionService.findById(orderResponse.optionId())),
                                orderResponse.quantity());
        wishlistService.deleteByUserIdProductId(userId, optionService.findById(orderResponse.optionId()).getProduct().getId());
        // 3. 카카오톡 메시지 api 전송
        orderService.sendMessage(jwtAccessToken, orderRequest.message());
        // 4. response 반환
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    @Operation(summary = "주문목록 조회")
    public ResponseEntity<Page<OrderResponse>> getWishlist(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "orderDateTime,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<OrderResponse> orderPages = orderService.getOrderResponses(pageable);
        return new ResponseEntity<>(orderPages, HttpStatus.OK);
    }
}
