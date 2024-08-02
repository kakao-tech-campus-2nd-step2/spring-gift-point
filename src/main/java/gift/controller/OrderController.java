package gift.controller;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;

import gift.domain.AppUser;
import gift.dto.common.CommonResponse;
import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {
    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "로그인한 사용자의 주문 생성",
            security = @SecurityRequirement(name = "JWT"))
    @PostMapping
    public ResponseEntity<?> createOrder(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                         @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(loginAppUser, orderRequest);
        kakaoService.sendMessageToMe(loginAppUser, orderRequest.message());
        return ResponseEntity.ok(new CommonResponse<>(orderResponse, "사용자의 주문이 성공적으로 완료되었습니다.", true));
    }

    @Operation(summary = "로그인한 사용자의 주문 목록 조회",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping
    public ResponseEntity<?> getUserOrders(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                           @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "wishCount", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                new CommonResponse<>(orderService.getUserOrders(loginAppUser.getId(), pageable),
                        "사용자의 주문 목록 조회가 성공적으로 완료되었습니다.",
                        true));
    }
}
