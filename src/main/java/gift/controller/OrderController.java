package gift.controller;

import gift.domain.Member;
import gift.dto.CommonResponse;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.JwtProvider;
import gift.service.MemberService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
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
public class OrderController {

    private final OrderService orderService;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public OrderController(JwtProvider jwtProvider, OrderService orderService, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.orderService = orderService;
        this.memberService = memberService;
    }

    @Operation(summary = "새로운 주문을 생성합니다")
    @PostMapping
    public ResponseEntity<CommonResponse> order(@RequestHeader("Authorization") String fullToken, @RequestBody OrderRequestDto orderRequestDto) {
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        OrderResponseDto orderResponseDto = orderService.order(orderRequestDto, memberEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(orderResponseDto, "주문 생성 성공", true));
    }

    @Operation(summary = "상품 주문 시 나에게 카카오톡 메세지 전송")
    @GetMapping("/message/list")
    public ResponseEntity<String> messageList(@RequestHeader("Authorization") String fullToken) {
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        return orderService.sendKakaoMessage(memberEmail);
    }

    @Operation(summary = "주문 목록을 페이지 단위로 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getPagedOrders(@RequestHeader("Authorization") String fullToken,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String sort,
        Pageable pageable) {
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        Member member = memberService.findByEmail(userEmail);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse(orderService.getPagedOrders(member, pageable), "페이징 된 주문 목록 조회 성공", true));
    }
}
